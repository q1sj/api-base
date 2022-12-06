package com.xsy.security.config;

import com.xsy.base.util.CollectionUtils;
import com.xsy.base.util.RequestMappingUtils;
import com.xsy.security.annotation.NoAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * NoAuth注解扫描类
 * {@link NoAuth}
 *
 * @author Q1sj
 * @date 2022.10.27 14:10
 */
@Slf4j
@Component
public class NoAuthScan implements ApplicationContextAware {

    private final Map<String, String> noAuthMap = new LinkedHashMap<>();

    public Map<String, String> getNoAuthMap() {
        return noAuthMap;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        //遍历每个controller层
        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object value = entry.getValue();
            Class<?> controllerClass = AopUtils.getTargetClass(value);
            Method[] methods = controllerClass.getMethods();
            for (Method method : methods) {
                NoAuth noAuth = AnnotatedElementUtils.findMergedAnnotation(method, NoAuth.class);
                if (noAuth == null) {
                    continue;
                }
                List<String> paths = RequestMappingUtils.getPaths(method);
                if (CollectionUtils.isEmpty(paths)) {
                    log.warn("method:{} path为空 @NoAuth无效", method);
                    continue;
                }
                log.debug("no auth method:{} paths:{}", method, paths);
                for (String path : paths) {
                    noAuthMap.put(path, DefaultFilter.anon.name());
                }
            }
        }
    }
}
