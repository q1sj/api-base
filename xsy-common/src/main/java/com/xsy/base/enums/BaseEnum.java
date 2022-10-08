package com.xsy.base.enums;

/**
 * 基础枚举类
 * {@link com.xsy.base.util.EnumUtils}
 *
 * @author Q1sj
 * @date 2022.8.31 9:49
 */
public interface BaseEnum<V, D> {
    V getValue();

    D getDesc();
}
