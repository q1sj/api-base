package com.xsy.sys.service;

import com.xsy.sys.entity.SysTaskConfigEntity;

/**
 * @author Q1sj
 * @date 2023.3.7 13:42
 */
public interface DynamicTask extends Runnable {
    String DEFAULT_CRON_EXPRESSION = "0 0/1 * * * ?";

    /**
     * 默认配置
     * 数据库配置不存在时使用
     *
     * @return
     */
    default SysTaskConfigEntity getDefaultConfig() {
        SysTaskConfigEntity sysTaskConfigEntity = new SysTaskConfigEntity();
        sysTaskConfigEntity.setTaskName(this.getClass().getName());
        sysTaskConfigEntity.setEnable(false);
        sysTaskConfigEntity.setCronExpression(DEFAULT_CRON_EXPRESSION);
        return sysTaskConfigEntity;
    }
}
