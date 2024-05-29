package com.xsy.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration("com.xsy.base.config.RestTemplateConfig")
public class RestTemplateConfig {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        // 支持中文编码
        restTemplate.getMessageConverters().set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;

    }

    @Bean
    @ConditionalOnMissingBean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        return createDefaultClientHttpRequestFactory();
    }

    public static ThreadPoolExecutor createHttpThreadPool() {
        return new ThreadPoolExecutor(
                10, Runtime.getRuntime().availableProcessors() * 10,
                1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(10),
                new CustomizableThreadFactory("async-http-"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    public ThreadPoolExecutor httpThreadPool() {
        return createHttpThreadPool();
    }

    public static RestTemplate createDefaultRestTemplate() {
        return new RestTemplate(createDefaultClientHttpRequestFactory());
    }

    public static ClientHttpRequestFactory createDefaultClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //单位为ms
        factory.setReadTimeout(10000);
        //单位为ms
        factory.setConnectTimeout(10000);
        return factory;
    }
}
