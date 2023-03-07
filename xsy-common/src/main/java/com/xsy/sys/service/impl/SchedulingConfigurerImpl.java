package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.util.CollectionUtils;
import com.xsy.sys.dao.SysTaskConfigDao;
import com.xsy.sys.entity.SysTaskConfigEntity;
import com.xsy.sys.service.DynamicTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Q1sj
 * @date 2023.3.7 13:47
 */
@Slf4j
@Component
public class SchedulingConfigurerImpl implements SchedulingConfigurer {
    @Autowired(required = false)
    private List<DynamicTask> dynamicTaskList;
    @Autowired
    private SysTaskConfigDao sysTaskConfigDao;
    @Autowired
    private ScheduledExecutorService dynamicScheduleThreadPool;
    private ScheduledTaskRegistrar taskRegistrar;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        this.taskRegistrar = taskRegistrar;
        taskRegistrar.setScheduler(dynamicScheduleThreadPool);
        if (CollectionUtils.isEmpty(dynamicTaskList)) {
            return;
        }
        for (DynamicTask dynamicTask : dynamicTaskList) {
            registrarTask(dynamicTask);
        }
    }

    public void registrarTask(DynamicTask dynamicTask) {
        taskRegistrar.addTriggerTask(new CanStopedRunnable(dynamicTask), new DynamicTrigger(dynamicTask));
    }

    private SysTaskConfigEntity getTaskConfig(DynamicTask dynamicTask) {
        LambdaQueryWrapper<SysTaskConfigEntity> wrapper = Wrappers.lambdaQuery(SysTaskConfigEntity.class)
                .eq(SysTaskConfigEntity::getTaskName, dynamicTask.getClass().getName());
        SysTaskConfigEntity config = null;
        try {
            config = sysTaskConfigDao.selectOne(wrapper);
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

    class CanStopedRunnable implements Runnable {
        private final DynamicTask dynamicTask;

        public CanStopedRunnable(DynamicTask dynamicTask) {
            this.dynamicTask = dynamicTask;
        }

        @Override
        public void run() {
            SysTaskConfigEntity taskConfig = getTaskConfig(dynamicTask);
            String taskClassName = dynamicTask.getClass().getName();
            if (!taskConfig.getEnable()) {
                log.warn("{} enable = false", taskClassName);
                return;
            }
            long start = System.currentTimeMillis();
            log.info("{}任务开始...", taskClassName);
            try {
                dynamicTask.run();
            } catch (Exception e) {
                log.error("task:{}运行异常 {}", taskClassName, e.getMessage(), e);
            } finally {
                log.info("{}任务结束...耗时:{}ms", taskClassName, System.currentTimeMillis() - start);
            }
        }
    }

    class DynamicTrigger implements Trigger {
        private final DynamicTask dynamicTask;

        public DynamicTrigger(DynamicTask dynamicTask) {
            this.dynamicTask = dynamicTask;
        }

        @Override
        public Date nextExecutionTime(TriggerContext triggerContext) {
            SysTaskConfigEntity config = getTaskConfig(dynamicTask);
            String cronExpression = config.getCronExpression();
            if (!CronSequenceGenerator.isValidExpression(cronExpression)) {
                log.warn("cron无效:{} 使用默认cron:{}", cronExpression, DynamicTask.DEFAULT_CRON_EXPRESSION);
                cronExpression = DynamicTask.DEFAULT_CRON_EXPRESSION;
            }
            return new CronTrigger(cronExpression).nextExecutionTime(triggerContext);
        }
    }
}
