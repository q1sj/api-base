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
    /**
     * excel文件名 默认uuid
     *
     * @return
     */
    String filename() default "";

    /**
     * 获取要导出数据的spel表达式 例如#reseult.getData()
     * #reseult为返回值
     * 表达式为空时使用默认方法解析返回值
     *
     * @return
     */
    String resultExpression() default "";

    /**
     * 导出表头class 默认List第0个元素的class
     *
     * @return
     */
    Class<?> exportClass() default Object.class;
}
