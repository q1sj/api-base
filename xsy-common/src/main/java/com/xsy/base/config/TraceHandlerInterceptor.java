package com.xsy.base.config;

import brave.Tracer;
import brave.propagation.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Q1sj
 * @date 2022.5.5 9:53
 */
@Component
public class TraceHandlerInterceptor implements HandlerInterceptor {
    @Autowired
    private Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        TraceContext context = tracer.currentSpan().context();
        response.setHeader("Trace-Id", context.traceIdString());
        return true;
    }
}
