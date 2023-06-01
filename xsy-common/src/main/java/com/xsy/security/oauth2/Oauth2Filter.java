/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.oauth2;


import com.xsy.base.enums.ResultCodeEnum;
import com.xsy.base.util.*;
import com.xsy.security.annotation.NoAuth;
import com.xsy.security.enums.SecurityConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * oauth2过滤器
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
public class Oauth2Filter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token
        String token = getRequestToken((HttpServletRequest) request);

        if (StringUtils.isBlank(token)) {
            return null;
        }

        return new Oauth2Token(token);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringContextUtils.getBean(RequestMappingHandlerMapping.class);
        try {
            Annotation[] declaredAnnotations = Optional.ofNullable(requestMappingHandlerMapping.getHandler((HttpServletRequest) request))
                    .map(handler -> ((HandlerMethod) handler.getHandler()))
                    .map(HandlerMethod::getMethod)
                    .map(Method::getDeclaredAnnotations)
                    .orElse(null);
	        if (CollectionUtils.isEmpty(declaredAnnotations)) {
		        return false;
	        }
            for (Annotation annotation : declaredAnnotations) {
                if (annotation.annotationType() == NoAuth.class) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest) request);
        if (StringUtils.isBlank(token)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setContentType("application/json;charset=utf-8");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());

            String json = JsonUtils.toJsonString(Result.error(ResultCodeEnum.NO_LOGIN));

            httpResponse.getWriter().print(json);

            return false;
        }

        return executeLogin(request, response);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.warn(e.getMessage(), e);
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
        try {
            //处理登录失败的异常
            Result<?> r;
            ResultCodeEnum resultCodeEnum;
            if (e instanceof ExpiredCredentialsException) {
                resultCodeEnum = ResultCodeEnum.LOGIN_EXPIRED;
            } else {
                resultCodeEnum = ResultCodeEnum.AUTHENTICATE_FAIL;
            }
            r = Result.error(resultCodeEnum);
            String json = JsonUtils.toJsonString(r);
            httpResponse.getWriter().print(json);
        } catch (IOException ignore) {

        }

        return false;
    }

    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        //从header中获取token
        String token = httpRequest.getHeader(SecurityConstant.TOKEN_HEADER);

        //如果header中不存在token，则从参数中获取token
        if (StringUtils.isBlank(token)) {
            token = httpRequest.getParameter(SecurityConstant.TOKEN_HEADER);
        }

        return token;
    }

}
