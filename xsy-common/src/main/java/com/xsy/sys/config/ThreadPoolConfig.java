package com.xsy.sys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2023.3.2 8:51
 */
@Component("com.xsy.sys.config.ThreadPoolConfig")
@Configuration
public class ThreadPoolConfig {
    @Value("${thread-pool.task-scheduler.pool-size:20}")
    private Integer threadPoolTaskSchedulerPoolSize;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(threadPoolTaskSchedulerPoolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("task-");
        threadPoolTaskScheduler.initialize();

        return threadPoolTaskScheduler;
    }

    @Bean
    public ExecutorService exportThreadPool() {
        return new ThreadPoolExecutor(10, 10, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new CustomizableThreadFactory("export-"),
                new ThreadPoolExecutor.DiscardPolicy());
    }
}
