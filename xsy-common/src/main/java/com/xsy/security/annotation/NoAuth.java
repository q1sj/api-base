package com.xsy.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解的Controller类和Controller方法 跳过认证授权
 * {@link com.xsy.security.oauth2.Oauth2Filter#isAccessAllowed}
 *
 * @author Q1sj
 * @date 2022.10.26 15:51
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface NoAuth {
}
