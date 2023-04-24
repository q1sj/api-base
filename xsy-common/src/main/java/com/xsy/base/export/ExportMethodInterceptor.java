package com.xsy.base.export;

import com.xsy.base.util.*;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

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
        List list = parseList(result, export);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        Class exportClass = export.exportClass() == Object.class ? list.get(0).getClass() : export.exportClass();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletResponse response = servletRequestAttributes.getResponse();
            ExcelUtils.excelDown(response, exportClass, list, StringUtils.isNotBlank(export.filename()) ? export.filename() : UUID.randomUUID().toString());
        }
        return null;
    }

    /**
     * 解析列表
     *
     * @param result
     * @param export
     * @return {@link List}
     */
    private List parseList(Object result, Export export) {
        List list = null;
        Object data = result;
        if (StringUtils.isNotBlank(export.resultExpression())) {
            SpelExpressionParser spelParser = new SpelExpressionParser();
            Expression spel = spelParser.parseExpression(export.resultExpression());
            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable("result", result);
            data = spel.getValue(context);
        } else if (result instanceof Result) {
            data = ((Result) result).getData();
        }
        if (data instanceof Collection) {
            list = new ArrayList((Collection) data);
        } else if (data instanceof PageData) {
            list = ((PageData) data).getList();
        } else {
            list = Collections.singletonList(data);
        }
        return list;
    }
}
