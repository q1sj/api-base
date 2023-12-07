package com.xsy.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xsy.base.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;

/**
 * @author Q1sj
 * @date 2022.9.26 14:57
 */
@Slf4j
@Component
public class HttpUtils {

    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            10, Runtime.getRuntime().availableProcessors() * 10,
            1, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(10),
            new CustomizableThreadFactory("async-http-"),
            new ThreadPoolExecutor.CallerRunsPolicy());
    private static RestTemplate restTemplate = new RestTemplate();

    public HttpUtils(RestTemplate restTemplate) {
        // Spring实例化 覆盖默认restTemplate
        HttpUtils.restTemplate = restTemplate;
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) throws GlobalException {
        BizAssertUtils.isNotBlank(url, "url不能为空");
        BizAssertUtils.isNotNull(httpMethod, "httpMethod不能为空");
        BizAssertUtils.isNotNull(respType, "respType不能为空");

        long startTime = System.currentTimeMillis();
        T resp = null;
        try {
            ResponseEntity<String> respEntity = restTemplate.exchange(url, httpMethod, body, String.class);
            log.debug("resp:{}", respEntity);
            String respBody = respEntity.getBody();
            resp = JsonUtils.parseObject(respBody, respType);
            return resp;
        } catch (Exception e) {
            throw new GlobalException(url + "请求失败", e);
        } finally {
            log.info("cost:{}ms {} url:{} body:{} resp:{}", System.currentTimeMillis() - startTime, httpMethod, url, body, resp == null ? "" : JsonUtils.toLogJsonString(resp));
        }
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, Class<T> respType) throws GlobalException {
        BizAssertUtils.isNotBlank(url, "url不能为空");
        BizAssertUtils.isNotNull(httpMethod, "httpMethod不能为空");
        BizAssertUtils.isNotNull(respType, "respType不能为空");

        long startTime = System.currentTimeMillis();
        T resp = null;
        try {
            ResponseEntity<T> respEntity = restTemplate.exchange(url, httpMethod, body, respType);
            log.debug("resp:{}", respEntity);
            resp = respEntity.getBody();
            return resp;
        } catch (Exception e) {
            throw new GlobalException(url + "请求失败", e);
        } finally {
            log.info("cost:{}ms {} url:{} body:{} resp:{}", System.currentTimeMillis() - startTime, httpMethod, url, body, resp == null || String.class == respType ? resp : JsonUtils.toLogJsonString(resp));
        }
    }
    public static <T> Future<T> asyncExchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) {
        return THREAD_POOL.submit(() -> exchange(url, httpMethod, body, respType));
    }
}
