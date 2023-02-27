package com.xsy.security.config;

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

/**
 * 可用权限注解扫描
 *
 * @author Q1sj
 */
@Slf4j
@Component
public class RequiresPermissionsScan implements ApplicationContextAware {
    private List<String> permissionList;

    public List<String> getPermissionList() {
        return Collections.unmodifiableList(permissionList);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        Set<String> permissionsSet = new LinkedHashSet<>();
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
                log.debug("method:{} permissions:{}", method.getName(), permissions.value());
                permissionsSet.addAll(Arrays.asList(permissions.value()));
            }
        }
        permissionList = new ArrayList<>(permissionsSet);
    }
}
