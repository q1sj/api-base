package com.xsy.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 图表
 *
 * @author Q1sj
 * @date 2022.9.1 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChartDTO<L, V> {
    private L label;
    private V value;

    public static <L, V, T> List<ChartDTO<L, V>> init(T[] arr, Function<T, L> getLabel, Function<T, V> getValue) {
        if (arr == null || arr.length == 0) {
            return Collections.emptyList();
        }
        return init(Stream.of(arr).collect(Collectors.toList()), getLabel, getValue);
    }

    public static <L, V, T> List<ChartDTO<L, V>> init(Collection<T> list, Function<T, L> getLabel, Function<T, V> getValue) {
        Objects.requireNonNull(getLabel);
        Objects.requireNonNull(getValue);
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<ChartDTO<L, V>> l = new ArrayList<>();
        for (T t : list) {
            l.add(new ChartDTO<>(getLabel.apply(t), getValue.apply(t)));
        }
        return l;
    }
}
