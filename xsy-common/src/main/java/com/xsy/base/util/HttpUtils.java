package com.xsy.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.xsy.base.config.RestTemplateConfig;
import com.xsy.base.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author Q1sj
 * @date 2022.9.26 14:57
 */
@Slf4j
@Component
public class HttpUtils {
    /**
     * 接口熔断时间(秒)
     */
    private static int fuseTime = 5;
    /**
     * 请求失败url
     */
    private static LoadingCache<String, Boolean> requestFailUrlCache = createCache();

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            10, Runtime.getRuntime().availableProcessors() * 10,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(10),
            new CustomizableThreadFactory("async-http-"),
            new ThreadPoolExecutor.CallerRunsPolicy());
    private static RestTemplate restTemplate = RestTemplateConfig.createDefaultRestTemplate();

    public HttpUtils(RestTemplate restTemplate) {
        // Spring实例化 覆盖默认restTemplate
        HttpUtils.restTemplate = restTemplate;
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) throws GlobalException {
        String resp = exchange(url, httpMethod, body, String.class);
        return JsonUtils.parseObject(resp, respType);
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, Class<T> respType) throws GlobalException {
        BizAssertUtils.isNotBlank(url, "url不能为空");
        BizAssertUtils.isNotNull(httpMethod, "httpMethod不能为空");
        BizAssertUtils.isNotNull(respType, "respType不能为空");
        if (Objects.equals(requestFailUrlCache.get(url), true)) {
            throw new GlobalException(url + "请求失败 " + fuseTime + "后重试");
        }
        long startTime = System.currentTimeMillis();
        T resp = null;
        try {
            ResponseEntity<T> respEntity = restTemplate.exchange(url, httpMethod, body, respType);
            log.debug("resp:{}", respEntity);
            resp = respEntity.getBody();
            return resp;
        } catch (Exception e) {
            requestFailUrlCache.put(url, true);
            throw new GlobalException(url + "请求失败", e);
        } finally {
            log.info("cost:{}ms {} url:{} body:{} resp:{}", System.currentTimeMillis() - startTime, httpMethod, url, body == null ? "" : getLogJson(body.getBody()), getLogJson(resp));
        }
    }

    public static <T> Future<T> asyncExchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) {
        return THREAD_POOL.submit(() -> exchange(url, httpMethod, body, respType));
    }

    private static String getLogJson(Object o) {
        if (o == null) {
            return "";
        }
        if (String.class.equals(o.getClass())) {
            return o.toString();
        }
        try {
            return JsonUtils.toLogJsonString(o);
        } catch (Exception e) {
            return Objects.toString(o);
        }
    }

    public static void setFuseTime(int fuseTime) {
        if (HttpUtils.fuseTime == fuseTime) {
            return;
        }
        HttpUtils.fuseTime = fuseTime;
        requestFailUrlCache = createCache();
    }

    private static LoadingCache<String, Boolean> createCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofSeconds(fuseTime))
                .build(key -> false);
    }
}
