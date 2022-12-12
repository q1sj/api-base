package com.xsy.base.util;

import com.xsy.base.exception.ParamValidationException;

import java.util.Collection;
import java.util.Objects;

/**
 * 业务断言
 *
 * @author Q1sj
 * @date 2022.9.7 15:21
 */
public class BizAssertUtils {

    public static void equals(Object o1, Object o2, String msg) {
        if (!Objects.equals(o1, o2)) {
            throw new ParamValidationException(msg);
        }
    }

    public static void isTrue(boolean condition, String msg) {
        if (!condition) {
            throw new ParamValidationException(msg);
        }
    }

    public static void isFalse(boolean condition, String msg) {
        isTrue(!condition, msg);
    }

    public static void isNotNull(Object o, String msg) {
        if (o == null) {
            throw new ParamValidationException(msg);
        }
    }

    public static void isNotBlank(String s, String msg) {
        if (StringUtils.isBlank(s)) {
            throw new ParamValidationException(msg);
        }
    }

    public static void isNotEmpty(Object[] arr, String msg) {
        if (CollectionUtils.isEmpty(arr)) {
            throw new ParamValidationException(msg);
        }
    }

    public static void isNotEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new ParamValidationException(msg);
        }
    }
}
