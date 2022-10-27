package com.xsy.security.config;

import com.xsy.security.annotation.NoAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.*;

/**
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
                RequestMapping classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerClass, RequestMapping.class);
                RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
                NoAuth noAuth = AnnotatedElementUtils.findMergedAnnotation(method, NoAuth.class);
                if (methodRequestMapping == null || noAuth == null) {
                    continue;
                }
                List<String> paths = getPaths(classRequestMapping, methodRequestMapping);
                log.debug("no auth method:{} paths:{}", method, paths);
                for (String path : paths) {
                    noAuthMap.put(path, "anon");
                }
            }
        }
    }


    private List<String> getPaths(RequestMapping classRequestMapping, RequestMapping methodRequestMapping) {
        List<String> classPaths = new ArrayList<>();
        if (classRequestMapping != null && classRequestMapping.path().length > 0) {
            for (String path : classRequestMapping.path()) {
                classPaths.add(path.startsWith("/") ? path : "/" + path);
            }
        }
        List<String> fullPaths = new ArrayList<>();
        for (String methodPath : methodRequestMapping.path()) {
            methodPath = methodPath.startsWith("/") ? methodPath : "/" + methodPath;
            if (classPaths.size() > 0) {
                for (String classPath : classPaths) {
                    fullPaths.add(classPath + methodPath);
                }
            } else {
                fullPaths.add(methodPath);
            }
        }
        return fullPaths;
    }
}
