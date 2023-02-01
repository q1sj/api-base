package com.xsy.base.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 特殊情况忽略统一日志打印
 * {@link ApiLogAop}
 *
 * @author Q1sj
 * @date 2022.9.22 15:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLog {
}
