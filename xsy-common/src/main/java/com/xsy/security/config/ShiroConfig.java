/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.config;

import com.xsy.security.oauth2.Oauth2Filter;
import com.xsy.security.oauth2.Oauth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Shiro的配置文件
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(Oauth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager,
                                             Optional<BaseAuthFilterMapConfig> baseAuthFilterMapConfig,
                                             NoAuthScan noAuthScan) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new Oauth2Filter());
        shiroFilter.setFilters(filters);
        Map<String, String> filterMap = baseAuthFilterMapConfig.map(BaseAuthFilterMapConfig::getFilterMap).orElse(new HashMap<>(1));
        // 注解标注的anon接口地址添加到手动配置中
        filterMap.putAll(noAuthScan.getNoAuthMap());
        filterMap.putIfAbsent("/**", "oauth2");
        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean
    public FilterRegistrationBean<Filter> filterRegistrationBean(AbstractShiroFilter shiroFilter) {
        FilterRegistrationBean<Filter> filterRegistration = new FilterRegistrationBean<>();
        filterRegistration.setFilter(shiroFilter);
        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
        filterRegistration.setAsyncSupported(true);
        filterRegistration.setEnabled(true);
        //这里添加一下对DispatcherType.ASYNC的支持就可以了
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);

        return filterRegistration;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
