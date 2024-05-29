package com.xsy.base.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 忽略方法日志打印 {@link ApiLogAop}
 * <br>
 * 忽略字段json日志打印 {@link com.xsy.base.util.JsonUtils#toLogJsonString(Object)}
 *
 * @author Q1sj
 * @date 2022.9.22 15:10
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLog {
}
