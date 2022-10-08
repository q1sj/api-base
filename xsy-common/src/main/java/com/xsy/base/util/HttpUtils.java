package com.xsy.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xsy.base.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Q1sj
 * @date 2022.9.26 14:57
 */
@Slf4j
public class HttpUtils {
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        restTemplate = SpringContextUtils.getBean(RestTemplate.class);
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, HttpEntity<Object> body, TypeReference<T> respType) throws GlobalException {
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
            log.info("{} url:{} body:{} resp:{} cost:{}ms", httpMethod.toString(), url, body, respBody, System.currentTimeMillis() - startTime);
        }
    }

    public static <T> Future<T> asyncExchange(String url, HttpMethod httpMethod, HttpEntity<Object> body, TypeReference<T> respType) {
        return threadPool.submit(() -> exchange(url, httpMethod, body, respType));
    }
}
