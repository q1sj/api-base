package com.xsy.base.export;

import com.xsy.base.exception.GlobalException;
import com.xsy.base.util.CollectionUtils;
import com.xsy.base.util.ExcelUtils;
import com.xsy.base.util.RequestMappingUtils;
import com.xsy.base.util.Result;
import com.xsy.security.annotation.NoAuth;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * NoAuth注解扫描类
 * {@link NoAuth}
 *
 * @author Q1sj
 * @date 2022.10.27 14:10
 */
@Slf4j
@Component
public class ExportScan implements ApplicationContextAware {
    @Autowired
    private RequestMappingHandlerMapping mapping;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class);
        //遍历每个controller层
        for (Map.Entry<String, Object> entry : controllers.entrySet()) {
            Object value = entry.getValue();
            Class<?> controllerClass = AopUtils.getTargetClass(value);
            Method[] methods = controllerClass.getMethods();
            // cglib增强
            Object exportController = null;
            for (Method method : methods) {
                Export export = AnnotatedElementUtils.findMergedAnnotation(method, Export.class);
                if (export == null) {
                    continue;
                }
                List<String> paths = RequestMappingUtils.getPaths(method);
                if (CollectionUtils.isEmpty(paths)) {
                    log.warn("method:{} path为空 @Export无效", method);
                    continue;
                }
                if (exportController == null) {
                    try {
                        exportController = createExportProxy(applicationContext, controllerClass);
                    } catch (Exception e) {
                        log.warn("{} cglib增强失败 @Export失效", controllerClass, e);
                        continue;
                    }
                }
                log.debug("export method:{} paths:{}", method, paths);
                // 注册
                for (String path : paths) {
                    RequestMappingInfo info = RequestMappingInfo.paths(path + "/export").methods(RequestMethod.GET).build();
                    mapping.registerMapping(info, exportController, method);
                    log.debug("registerMapping path:{}/export", path);
                }
            }
        }
    }

    private Object createExportProxy(ApplicationContext applicationContext, Class<?> controllerClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(controllerClass);
        enhancer.setCallback(new ExportMethodInterceptor(applicationContext, controllerClass));
        return enhancer.create();
    }

}