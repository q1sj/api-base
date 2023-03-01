package com.xsy.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.sys.dao.SysCronDao;
import com.xsy.sys.entity.SysCron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.scheduling.support.CronTrigger;

import java.util.Optional;

/**
 * @author Q1sj
 */
public abstract class AbstractDynamicSchedule implements SchedulingConfigurer {
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    private final String defaultCronExpression = "0 0/1 * * * ?" ;
    @Autowired
    private SysCronDao sysCronDao;

    public abstract void run();


    public String getDefaultCronExpression() {
        return defaultCronExpression;
    }

    public SysCron getTaskConfig() {
        LambdaQueryWrapper<SysCron> wrapper = Wrappers.lambdaQuery(SysCron.class)
                .eq(SysCron::getTaskName, getClass().getName());
        return Optional.ofNullable(sysCronDao.selectOne(wrapper)).orElseGet(() -> {
            log.warn("数据库配置不存在 使用默认配置");
            SysCron sysCron = new SysCron();
            sysCron.setEnable(false);
            sysCron.setCronExpression(getDefaultCronExpression());
            return sysCron;
        });
    }

    /**
     * 执行定时任务.
     */
    @Override
    public final void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    SysCron taskConfig = getTaskConfig();
                    long start = System.currentTimeMillis();
                    if (!taskConfig.getEnable()) {
                        log.warn("enable = false");
                        return;
                    }
                    log.info("{}定时任务开始... cron:{} ", getClass().getName(), taskConfig.getCronExpression());
                    try {
                        run();
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        log.info("{}定时任务结束...耗时:{}ms", getClass().getName(), System.currentTimeMillis() - start);
                    }
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    SysCron taskConfig = getTaskConfig();
                    String cron = taskConfig.getCronExpression();
                    if (!CronSequenceGenerator.isValidExpression(cron)) {
                        log.warn("cron不合法:{} 使用默认cron:{} set enable=false", cron, getDefaultCronExpression());
                        cron = getDefaultCronExpression();
                        taskConfig.setEnable(false);
                        sysCronDao.updateById(taskConfig);
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }
}