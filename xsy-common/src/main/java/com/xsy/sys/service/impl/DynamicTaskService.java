package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.util.CollectionUtils;
import com.xsy.base.util.SpringContextUtils;
import com.xsy.sys.entity.SysTaskConfigEntity;
import com.xsy.sys.entity.SysTaskLogEntity;
import com.xsy.sys.service.DynamicTask;
import com.xsy.sys.service.SysTaskConfigService;
import com.xsy.sys.service.SysTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @author Q1sj
 * @date 2023.3.8 9:07
 */
@Slf4j
@Service
public class DynamicTaskService implements CommandLineRunner {
    @Autowired(required = false)
    private List<DynamicTask> dynamicTaskList;
    @Autowired
    private SysTaskConfigService sysTaskConfigService;
    @Autowired
    private SysTaskLogService sysTaskLogService;
    @Autowired
    @Lazy
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {
        if (CollectionUtils.isEmpty(dynamicTaskList)) {
            return;
        }
        List<SysTaskConfigEntity> configList = sysTaskConfigService.list();
        if (CollectionUtils.isEmpty(configList)) {
            return;
        }
        Map<String, DynamicTask> map = dynamicTaskList.stream().collect(Collectors.toMap(t -> t.getClass().getName(), t -> t));
        for (SysTaskConfigEntity taskConfig : configList) {
            DynamicTask dynamicTask = map.get(taskConfig.getTaskName());
            Objects.requireNonNull(dynamicTask, taskConfig.getTaskName() + "未找到bean");
            add(dynamicTask, taskConfig);
        }
    }

    /**
     * 获取定时任务配置
     *
     * @param dynamicTask
     * @return
     */
    private SysTaskConfigEntity getTaskConfig(DynamicTask dynamicTask) {
        LambdaQueryWrapper<SysTaskConfigEntity> wrapper = Wrappers.lambdaQuery(SysTaskConfigEntity.class)
                .eq(SysTaskConfigEntity::getTaskName, dynamicTask.getClass().getName());
        SysTaskConfigEntity config = sysTaskConfigService.getOne(wrapper);
        if (config == null) {
            throw new GlobalException(dynamicTask.getClass().getName() + "定时任务配置不存在");
        }
        return config;
    }

    /**
     * 如果需要停止修改enable
     *
     * @param taskName
     */
    public void delete(String taskName) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.remove(taskName);
        if (scheduledFuture == null) {
            return;
        }
        log.debug("cancel:{}", taskName);
        scheduledFuture.cancel(false);
    }

    public void add(String taskName) {
        add(getDynamicTask(taskName));
    }

    /**
     * 添加定时任务,停止旧的定时任务
     *
     * @param dynamicTask
     */
    public void add(DynamicTask dynamicTask) {
        SysTaskConfigEntity taskConfig = getTaskConfig(dynamicTask);
        add(dynamicTask, taskConfig);
    }

    public DynamicTask getDynamicTask(String taskName) {
        try {
            Class<?> clazz = Class.forName(taskName);
            if (!DynamicTask.class.isAssignableFrom(clazz)) {
                throw new GlobalException("task类型不匹配:" + taskName);
            }
            return (DynamicTask) SpringContextUtils.getBean(clazz);
        } catch (BeansException | ClassNotFoundException e) {
            throw new GlobalException("taskName不存在:" + taskName, e);
        }
    }

    private void add(DynamicTask dynamicTask, SysTaskConfigEntity taskConfig) {
        String cronExpression;
        if (taskConfig.getEnable()) {
            cronExpression = taskConfig.getCronExpression();
        } else {
            cronExpression = CronSequenceGenerator.isValidExpression(taskConfig.getCronExpression()) ? taskConfig.getCronExpression() : "0 0/10 * * * ?";
        }
        CanStopedRunnable runnable = new CanStopedRunnable(dynamicTask);
        String taskName = runnable.taskName;
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(runnable,
                taskConfig.getAllowReFiresCron() ? new AllowReFiresCronTrigger(cronExpression, taskName) : new CronTrigger(cronExpression));
        scheduledFutureMap.compute(taskName, (k, v) -> {
            if (v != null) {
                log.debug("cancel old:{}", k);
                v.cancel(false);
            }
            return schedule;
        });
    }
    class CanStopedRunnable implements Runnable {
        private final DynamicTask dynamicTask;
        private final String taskName;

        public CanStopedRunnable(DynamicTask dynamicTask) {
            this.dynamicTask = dynamicTask;
            this.taskName = dynamicTask.getClass().getName();
        }

        @Override
        public void run() {
            SysTaskConfigEntity taskConfig = getTaskConfig(dynamicTask);
            if (!taskConfig.getEnable()) {
                log.warn("{} enable = false", taskName);
                return;
            }
            long start = System.currentTimeMillis();
            log.info("{}任务开始...", taskName);
            SysTaskLogEntity logEntity = new SysTaskLogEntity();
            logEntity.setTaskId(taskConfig.getId());
            logEntity.setTaskName(taskName);
            logEntity.setCreateTime(new Date());
            logEntity.setMsg("");
            logEntity.setStatus(SysTaskLogEntity.RUNNING_STATUS);
            // 先记录开始日志
            sysTaskLogService.save(logEntity);
            try {
                dynamicTask.run(taskConfig.getParam());
            } catch (Exception e) {
                log.error("task:{}运行异常 {}", taskName, e.getMessage(), e);
                logEntity.setStatus(SysTaskLogEntity.FAIL_STATUS);
                logEntity.setMsg(e.toString());
            } catch (Throwable t) {
                logEntity.setStatus(SysTaskLogEntity.FAIL_STATUS);
                logEntity.setMsg(t.toString());
                throw t;
            } finally {
                long cost = System.currentTimeMillis() - start;
                log.info("{}任务结束...耗时:{}ms", taskName, cost);
                // 运行记录写数据库
                logEntity.setCost((int) cost);
                // 更新耗时
                sysTaskLogService.saveOrUpdate(logEntity);
            }
        }
    }

    /**
     * 允许同一时间重复执行的cron触发器
     * 解决时钟回拨时定时任务延迟过久问题 根据业务考虑是否使用
     */
    @Slf4j
    static class AllowReFiresCronTrigger implements Trigger {
        private final CronSequenceGenerator sequenceGenerator;
        private final String taskName;

        public AllowReFiresCronTrigger(String expression, String taskName) {
            this.sequenceGenerator = new CronSequenceGenerator(expression);
            this.taskName = taskName;
        }

        @Override
        public Date nextExecutionTime(TriggerContext triggerContext) {
            Date next = sequenceGenerator.next(new Date());
            log.debug("taskName:{} lastCompletionTime:{} nextExecutionTime:{}", taskName, triggerContext.lastCompletionTime(), next);
            return next;
        }
    }
}
