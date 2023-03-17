package com.xsy.sys.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

/**
 * @author Q1sj
 * @date 2023.3.2 8:51
 */
@Component
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
}
