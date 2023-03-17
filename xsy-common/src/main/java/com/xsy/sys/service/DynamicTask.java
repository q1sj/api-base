package com.xsy.sys.service;

import com.xsy.sys.entity.SysTaskConfigEntity;

/**
 * @author Q1sj
 * @date 2023.3.7 13:42
 */
public interface DynamicTask extends Runnable {
    /**
     * 默认配置
     * 数据库配置不存在时使用
     *
     * @return
     */
    SysTaskConfigEntity getDefaultConfig();
}
