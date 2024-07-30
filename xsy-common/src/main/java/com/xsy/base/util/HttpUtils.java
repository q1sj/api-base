package com.xsy.base.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.xsy.base.config.RestTemplateConfig;
import com.xsy.base.enums.ResultCodeEnum;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.exception.RequestTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Q1sj
 * @date 2022.9.26 14:57
 */
@Slf4j
@Component
public class HttpUtils {
    /**
     * 日志报文最大长度
     * 该配置为兜底方案,不需要打印日志的字段应使用{@link com.xsy.base.log.IgnoreLog}注解,保持日志精简
     */
    public static int maxLogString = 50 * 1024;

    private static ExecutorService httpThreadPool = RestTemplateConfig.createHttpThreadPool();
    private static RestTemplate restTemplate = RestTemplateConfig.createDefaultRestTemplate();

    public HttpUtils(RestTemplate restTemplate, ExecutorService httpThreadPool) {
        // Spring实例化 覆盖默认restTemplate
        HttpUtils.restTemplate = restTemplate;
        HttpUtils.httpThreadPool = httpThreadPool;
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) throws GlobalException {
        String resp = exchange(url, httpMethod, body, String.class);
        return JsonUtils.parseObject(resp, respType);
    }

    public static <T> T exchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, Class<T> respType) throws GlobalException {
        BizAssertUtils.isNotBlank(url, "url不能为空");
        BizAssertUtils.isNotNull(httpMethod, "httpMethod不能为空");
        BizAssertUtils.isNotNull(respType, "respType不能为空");
        long startTime = System.currentTimeMillis();
        T resp = null;
        try {
            ResponseEntity<T> respEntity = restTemplate.exchange(url, httpMethod, body, respType);
            resp = respEntity.getBody();
            return resp;
        } catch (ResourceAccessException e) {
            throw new RequestTimeoutException(url + "请求超时", e);
        } catch (Exception e) {
            throw new GlobalException(ResultCodeEnum.THIRD_PARTY_SERVICES_ERROR, url + "请求失败", e);
        } finally {
            log.info("cost:{}ms {} url:{} body:{} resp:{}", System.currentTimeMillis() - startTime, httpMethod, url, body == null ? "" : getLogJson(body.getBody()), getLogJson(resp));
        }
    }

    public static <T> Future<T> asyncExchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, TypeReference<T> respType) {
        return httpThreadPool.submit(() -> exchange(url, httpMethod, body, respType));
    }

    public static <T> Future<T> asyncExchange(String url, HttpMethod httpMethod, @Nullable HttpEntity<Object> body, Class<T> respType) {
        return httpThreadPool.submit(() -> exchange(url, httpMethod, body, respType));
    }

    private static String getLogJson(Object o) {
        if (o == null) {
            return "";
        }
        String originLogString;
        if (String.class.equals(o.getClass())) {
            originLogString = o.toString();
        } else {
            try {
                originLogString = JsonUtils.toLogJsonString(o);
            } catch (Exception e) {
                originLogString = Objects.toString(o);
            }
        }
        return originLogString.length() > maxLogString
                ? originLogString.substring(0, maxLogString) + "...(长度:" + originLogString.length() + ")"
                : originLogString;
    }
}
