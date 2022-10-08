package com.xsy.security.config;

import com.xsy.base.cache.CacheManagerWrapper;
import com.xsy.base.cache.CacheWrapper;
import com.xsy.security.enums.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
@Component
public class PermissionsInit implements ApplicationContextAware {

    private final CacheWrapper cache;

    public PermissionsInit(CacheManagerWrapper cacheManagerWrapper) {
        cache = cacheManagerWrapper.getCache(SecurityConstant.PERMISSIONS_CACHE_NAME);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        Set<String> permissionsSet = new HashSet<>();
        //遍历每个controller层
        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object value = entry.getValue();
            Class<?> controllerClass = AopUtils.getTargetClass(value);
            log.debug("controller:{}", controllerClass);
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                RequiresPermissions permissions = AnnotationUtils.getAnnotation(method, RequiresPermissions.class);
                if (permissions == null) {
                    continue;
                }
                log.debug("method:{} permissions:{}",method.getName(),permissions.value());
                permissionsSet.addAll(Arrays.asList(permissions.value()));
            }
        }
        cache.put(SecurityConstant.PERMISSIONS_KEY, new ArrayList<>(permissionsSet));
    }
}
