package com.xsy.sys.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Q1sj
 * @date 2023.3.2 8:51
 */
@Component
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ScheduledExecutorService dynamicScheduleThreadPool() {
        return new ScheduledThreadPoolExecutor(20, new CustomizableThreadFactory("dynamicTask-"));
    }
}
