package com.xsy.base.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 将list分页 如果分页超出范围返回一个空的list
     *
     * @param list
     * @param page
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> List<T> getPagedList(List<T> list, int page, int pageSize) {
        if (isEmpty(list)) {
            return Collections.emptyList();
        }

        int totalElements = list.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        if (page < 1 || page > totalPages) {
            return Collections.emptyList();
        }

        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalElements);

        return list.stream()
                .skip(startIndex)
                .limit(endIndex - startIndex)
                .collect(Collectors.toList());
    }
}
