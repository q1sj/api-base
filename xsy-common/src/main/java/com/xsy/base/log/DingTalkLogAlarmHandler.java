package com.xsy.base.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.event.Level;

/**
 * @author Q1sj
 * @date 2022.10.17 10:40
 */
public class DingTalkLogAlarmHandler implements LogAlarmHandler {
    @Override
    public void handleWarn(ILoggingEvent eventObject) {

    }

    @Override
    public void handleError(ILoggingEvent eventObject) {

    }
}
