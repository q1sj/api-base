package com.xsy.base.export;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Q1sj
 * @date 2022.12.1 16:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Export {
    String filename() default "";
    /**
     * spel表达式 #reseult.getData()
     *
     * @return
     */
    String resultExpression() default "";

    Class<?>[] exportClass() default {};
}
