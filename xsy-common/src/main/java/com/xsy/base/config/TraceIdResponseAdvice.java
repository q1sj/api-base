package com.xsy.base.config;

import brave.Span;
import brave.Tracer;
import com.xsy.base.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Q1sj
 */
@RestControllerAdvice
public class TraceIdResponseAdvice implements ResponseBodyAdvice<Result<?>> {
    @Autowired
    private Tracer tracer;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return Result.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Result<?> beforeBodyWrite(Result<?> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Span span = tracer.currentSpan();
        if (body != null && span != null) {
            body.setTraceId(span.context().traceIdString());
        }
        return body;
    }
}