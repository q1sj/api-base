package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.util.CollectionUtils;
import com.xsy.base.util.SpringContextUtils;
import com.xsy.sys.entity.SysTaskConfigEntity;
import com.xsy.sys.service.DynamicTask;
import com.xsy.sys.service.SysTaskConfigService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

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
    @Lazy
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledFutureMap = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        if (CollectionUtils.isEmpty(dynamicTaskList)) {
            return;
        }
        for (DynamicTask dynamicTask : dynamicTaskList) {
            add(dynamicTask);
        }
    }

    private SysTaskConfigEntity getTaskConfig(DynamicTask dynamicTask) {
        LambdaQueryWrapper<SysTaskConfigEntity> wrapper = Wrappers.lambdaQuery(SysTaskConfigEntity.class)
                .eq(SysTaskConfigEntity::getTaskName, dynamicTask.getClass().getName());
        SysTaskConfigEntity config = null;
        try {
            config = sysTaskConfigService.getOne(wrapper);
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
        if (config != null) {
            return config;
        }
        SysTaskConfigEntity defaultConfig = dynamicTask.getDefaultConfig();
        log.debug("数据库配置不存在 使用默认配置:{}", defaultConfig);
        return defaultConfig;
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
    }

    public void add(String taskName) {
        add(getDynamicTask(taskName));
    }

    public void add(DynamicTask dynamicTask) {
        SysTaskConfigEntity taskConfig = getTaskConfig(dynamicTask);
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
        if (scheduledFutureMap.computeIfPresent(taskName, (k, v) -> {
            log.debug("cancel old:{}", k);
            v.cancel(false);
            return schedule;
        }) == null) {
            scheduledFutureMap.putIfAbsent(taskName, schedule);
        }
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
            try {
                dynamicTask.run();
            } catch (Exception e) {
                log.error("task:{}运行异常 {}", taskName, e.getMessage(), e);
            } finally {
                log.info("{}任务结束...耗时:{}ms", taskName, System.currentTimeMillis() - start);
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
