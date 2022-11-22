package com.xsy.security.config;

import java.util.Map;

/**
 * 授权路径配置
 * 创建子类并添加到ioc容器
 *
 * @author Q1sj
 * @date 2022.8.30 14:38
 */
public abstract class BaseAuthFilterMapConfig {
    /**
     * @return key:url,val:{@link org.apache.shiro.web.filter.mgt.DefaultFilter#anon}.name() 标识该接口地址跳过认证授权
     */
    public abstract Map<String, String> getFilterMap();
}
