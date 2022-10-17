package com.xsy.base.log;

import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * @author Q1sj
 * @date 2022.10.17 10:37
 */
public interface LogAlarmHandler {
    void handleWarn(ILoggingEvent eventObject);

    void handleError(ILoggingEvent eventObject);
}
