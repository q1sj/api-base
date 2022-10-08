package com.xsy.base.util;

import com.xsy.base.enums.BaseEnum;
import com.xsy.base.pojo.ChartDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Q1sj
 * @date 2022.2.16 10:57
 */
public class EnumUtils {

    public static <E extends BaseEnum<V, D>, V, D> D getDescByValue(Class<E> enumClass, V value, D defaultDesc) {
        D desc = getDescByValue(enumClass, value);
        return desc != null ? desc : defaultDesc;
    }

    public static <E extends BaseEnum<V, D>, V, D> D getDescByValue(Class<E> enumClass, V value) {
        E e = getByValue(enumClass, value);
        if (e == null) {
            return null;
        }
        return e.getDesc();
    }

    public static <E extends BaseEnum<V, D>, V, D> V getValueByDesc(Class<E> enumClass, D desc) {
        E e = getByDesc(enumClass, desc);
        if (e == null) {
            return null;
        }
        return e.getValue();
    }

    public static <E extends BaseEnum<V, D>, V, D> E getByValue(Class<E> enumClass, V value) {
        if (enumClass.isEnum()) {
            for (E baseEnum : enumClass.getEnumConstants()) {
                if (Objects.equals(baseEnum.getValue(), value)) {
                    return baseEnum;
                }
            }
        }
        return null;
    }

    public static <E extends BaseEnum<V, D>, V, D> E getByDesc(Class<E> enumClass, D desc) {
        if (enumClass.isEnum()) {
            for (E baseEnum : enumClass.getEnumConstants()) {
                if (Objects.equals(baseEnum.getDesc(), desc)) {
                    return baseEnum;
                }
            }
        }
        return null;
    }

    public static <D, V> List<ChartDTO<D, V>> enumToCharts(Class<? extends BaseEnum<V, D>> enumClass) {
        if (enumClass.isEnum()) {
            BaseEnum<V, D>[] enumConstants = enumClass.getEnumConstants();
            return ChartDTO.init(enumConstants, BaseEnum::getDesc, BaseEnum::getValue);
        }
        return Collections.emptyList();
    }
}
