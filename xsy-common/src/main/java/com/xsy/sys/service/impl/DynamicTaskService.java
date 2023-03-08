package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.util.CollectionUtils;
import com.xsy.sys.dao.SysTaskConfigDao;
import com.xsy.sys.entity.SysTaskConfigEntity;
import com.xsy.sys.service.DynamicTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

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
    private SysTaskConfigDao sysTaskConfigDao;
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

    public void delete(String taskName) {
        ScheduledFuture<?> scheduledFuture = scheduledFutureMap.remove(taskName);
        if (scheduledFuture == null) {
            return;
        }
        log.debug("cancel:{}", taskName);
        scheduledFuture.cancel(false);
    }

    public void add(DynamicTask dynamicTask) {
        SysTaskConfigEntity taskConfig = getTaskConfig(dynamicTask);
        String cronExpression = taskConfig.getCronExpression();
        add(new CanStopedRunnable(dynamicTask), cronExpression);
    }

    private void add(CanStopedRunnable runnable, String cron) {
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(runnable, new CronTrigger(cron));
        String key = runnable.taskName;
        if (scheduledFutureMap.computeIfPresent(key, (k, v) -> {
            log.debug("cancel old:{}", k);
            v.cancel(false);
            return schedule;
        }) == null) {
            scheduledFutureMap.putIfAbsent(key, schedule);
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
}
