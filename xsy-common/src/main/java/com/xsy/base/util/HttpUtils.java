package com.xsy.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xsy.base.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Q1sj
 * @date 2022.9.26 14:57
 */
@Slf4j
@Component
public class HttpUtils {
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static RestTemplate restTemplate;

    public HttpUtils(RestTemplate restTemplate) {
        HttpUtils.restTemplate = restTemplate;
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) throws GlobalException {
        BizAssertUtils.isNotNull(restTemplate, "restTemplate未初始化");
        BizAssertUtils.isNotBlank(url, "url不能为空");
        BizAssertUtils.isNotNull(httpMethod, "httpMethod不能为空");
        BizAssertUtils.isNotNull(respType, "respType不能为空");

        long startTime = System.currentTimeMillis();
        String respBody = null;
        try {
            ResponseEntity<String> resp = restTemplate.exchange(url, httpMethod, body, String.class);
            log.debug("resp:{}", resp);
            respBody = resp.getBody();
            return JsonUtils.parseObject(respBody, respType);
        } catch (Exception e) {
            throw new GlobalException(url + "请求失败", e);
        } finally {
            log.info("cost:{}ms {} url:{} body:{} resp:{}", System.currentTimeMillis() - startTime, httpMethod, url, body, respBody);
        }
    }

    public static <T> Future<T> asyncExchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) {
        return threadPool.submit(() -> exchange(url, httpMethod, body, respType));
    }
}
