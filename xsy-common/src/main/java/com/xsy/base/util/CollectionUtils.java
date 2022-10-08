package com.xsy.base.util;

import java.util.Collection;

/**
 * @author Q1sj
 * @date 2022.9.27 14:14
 */
public class CollectionUtils extends org.springframework.util.CollectionUtils {
    public static boolean isEmpty(Object[] arr) {
        return arr == null || arr.length <= 0;
    }

    public static boolean isNotEmpty(Object[] arr) {
        return !isEmpty(arr);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}
