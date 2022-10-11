package com.xsy.base.config;

import com.xsy.base.log.MyLogAppender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Q1sj
 * @date 2022.10.11 11:17
 */
@Component
@Configuration
public class LogConfig {
    @Bean
    public MyLogAppender myLogAppender() {
        return  MyLogAppender.getInstance();
    }
}
