package com.xsy.base.util;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Q1sj
 * @date 2022.12.6 10:58
 */
public class RequestMappingUtils {

    /**
     * 根据类,方法注解拼接完整接口路径
     *
     * @param controllerMethod
     * @return
     */
    public static List<String> getPaths(Method controllerMethod) {
        RequestMapping classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerMethod.getDeclaringClass(), RequestMapping.class);
        RequestMapping methodRequestMapping = AnnotatedElementUtils.findMergedAnnotation(controllerMethod, RequestMapping.class);
        List<String> classPaths = new ArrayList<>();
        if (classRequestMapping != null && CollectionUtils.isNotEmpty(classRequestMapping.path())) {
            for (String path : classRequestMapping.path()) {
                classPaths.add(path.startsWith("/") ? path : "/" + path);
            }
        }
        if (methodRequestMapping == null) {
            return classPaths;
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
