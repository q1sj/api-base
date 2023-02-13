package com.xsy.base.config;

import com.xsy.base.log.DingTalkLogAlarmHandler;
import com.xsy.base.log.LogAlarmHandler;
import com.xsy.base.log.LogMonitorAppender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Q1sj
 * @date 2022.10.11 11:17
 */
@Slf4j
//@Component
//@Configuration
public class LogConfig {
    //        @Bean
    public LogAlarmHandler logAlarmHandler() {
        return new DingTalkLogAlarmHandler();
    }

    @Bean
    @ConditionalOnBean(LogAlarmHandler.class)
    public LogMonitorAppender baseLogMonitorAppender(LogAlarmHandler logAlarmHandler) {
        log.debug("logAlarmHandler:{}", logAlarmHandler);
        return new LogMonitorAppender(logAlarmHandler);
    }
}
