package com.xsy.base.config;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.xsy.base.log.LogAlarmHandler;
import com.xsy.base.log.LogMonitorAppender;
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
    public LogAlarmHandler logAlarmHandler() {
//        return new DingTalkLogAlarmHandler();
        return new DoNothingLogAlarmHandler();
    }

    @Bean
    public LogMonitorAppender baseLogMonitorAppender(LogAlarmHandler logAlarmHandler) {
        return new LogMonitorAppender(logAlarmHandler);
    }

    static class DoNothingLogAlarmHandler implements LogAlarmHandler {

        @Override
        public void handleWarn(ILoggingEvent eventObject) {

        }

        @Override
        public void handleError(ILoggingEvent eventObject) {

        }
    }
}
