package com.xsy.base.export;

import com.xsy.base.exception.GlobalException;
import com.xsy.base.export.Export;
import com.xsy.base.util.*;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Q1sj
 * @date 2022.12.2 9:46
 */
public class ExportMethodInterceptor implements MethodInterceptor {
    private final ApplicationContext applicationContext;
    private final Class<?> controllerClass;

    public ExportMethodInterceptor(ApplicationContext applicationContext, Class<?> controllerClass) {
        this.applicationContext = applicationContext;
        this.controllerClass = controllerClass;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = proxy.invoke(applicationContext.getBean(controllerClass), args);
        Export export = AnnotatedElementUtils.findMergedAnnotation(method, Export.class);
        if (result == null || export == null) {
            return result;
        }
        List list = null;
        Object data = result;
        if (result instanceof Result) {
            data = ((Result) result).getData();
        }
        if (data instanceof List) {
            list = (List) data;
        } else if (data instanceof PageData) {
            list = ((PageData) data).getList();
        } else {
            list = Collections.singletonList(result);
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        ExcelUtils.excelDown(response, list.get(0).getClass(), list, StringUtils.isNotBlank(export.filename()) ? export.filename() : UUID.randomUUID().toString());
        return null;
    }
}
