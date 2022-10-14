package com.xsy.base;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.Assert;
import org.junit.rules.Stopwatch;
import org.springframework.util.StopWatch;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2022.9.13 16:59
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
//        expireAfterAccess();
//        maximumSize();
        int i = integerReplacement(7);
        for (int j = 1; j < 999; j++) {
            StopWatch stopWatch = new StopWatch(Objects.toString(j));
            stopWatch.start("method1");
            int i1 = integerReplacement(j);
            stopWatch.stop();

            stopWatch.start("method2");
            int i2 = integerReplacement2(j);
            stopWatch.stop();

            System.out.println(stopWatch.prettyPrint());
            System.out.println(i1 == i2);
        }
    }

    public static int integerReplacement(int n) {
        long l = n;
        int count = 0;
        while (l > 1) {
            // 奇数
            if (l % 2 != 0) {
                // l-1/2是奇数的 +1   l-1/2是偶数的 -1
                if (l != 3 && (l - 1) / 2 % 2 == 1) {
                    l++;
                } else {
                    l--;
                }
            } else {
                l = l / 2;
            }
//            System.out.println(l);
            count++;
        }
        return count;
    }

    public static int integerReplacement2(int n) {
        if (n == 1) {
            return 0;
        }
        if (n % 2 == 0) {
            return integerReplacement(n / 2) + 1;
        } else {
            int a = integerReplacement(n + 1);
            int b = integerReplacement(n - 1);
            return Math.min(a, b) + 1;
        }
    }


    public static void expireAfterAccess() throws Exception {
        System.out.println("----expireAfterAccess------");
        Cache<Object, Object> cache = Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(1, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                .build();
        cache.put("a", 1);
        TimeUnit.SECONDS.sleep(2);
        cache.put("b", 2);
        Object a = cache.getIfPresent("a");
        System.out.println(a);
    }

    public static void maximumSize() throws Exception {
        System.out.println("----maximumSize------");
        Cache<Object, Object> cache = Caffeine.newBuilder()
                .maximumSize(0)
                .build();
        cache.put("a", 1);
        cache.put("b", 2);
        System.out.println(cache.getIfPresent("a"));
        System.out.println(cache.getIfPresent("b"));
    }

    public static boolean wordBreak(String s, List<String> wordDict) {
//        int count = 0;
        Set<String> oldSet = null;
        Set<String> newSet = Collections.singleton(s);
        // newSet.removeAll(oldSet)后size=0 说明全部删除无法替换 return false
        while (newSet.size() > 0) {
            //遍历set 遍历wordDict s.replaceFirst后加入set
            oldSet = newSet;
            newSet = new HashSet<>();
            for (String s1 : oldSet) {
                for (String word : wordDict) {
//                    count++;
                    if (s1.equals(word)) {
//                        System.out.println(count);
                        return true;
                    }
                    //遍历wordDict s.startWith true  删除word后加入set
                    if (s1.startsWith(word)) {
                        newSet.add(s1.substring(word.length()));
                    }
                }
            }
            newSet.removeAll(oldSet);
        }
//        System.out.println(count);
        return false;
    }

    public static boolean wordBreak3(String s, List<String> wordDict) {
        int count = 0;
        int len = s.length();
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        for (int i = 1; i <= len; ++i) {
            for (int j = 0; j < i; ++j) {
                count++;
                if (dp[j] && wordDict.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        System.out.println(count);
        return dp[len];
    }

    public static boolean wordBreak2(String s, List<String> wordDict) {
        for (String word : wordDict) {
            if (s.startsWith(word)) {
//                System.out.println(word);
                String s1 = s.replaceFirst(word, "");
                if ("".equals(s1)) {
                    return true;
                }
                if (wordBreak(s1, wordDict)) {
                    return true;
                }
            }
        }
        return false;
    }

}
