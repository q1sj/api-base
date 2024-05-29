package com.xsy.sys.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Q1sj
 * @date 2023.3.24 16:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SysConfig {
    /**
     * 参数管理key 默认使用字段名
     */
    String value() default "";
}
