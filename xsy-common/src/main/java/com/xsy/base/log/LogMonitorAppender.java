package com.xsy.base.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author Q1sj
 * @date 2022.4.29 9:18
 */
public class LogMonitorAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    protected Logger log = LoggerFactory.getLogger(getClass());

    private final LogAlarmHandler logAlarmHandler;

    private boolean init = false;

    public LogMonitorAppender(LogAlarmHandler logAlarmHandler) {
        this.logAlarmHandler = logAlarmHandler;
    }

    @PostConstruct
    public void init() {
        if (init) {
            return;
        }
        synchronized (LogMonitorAppender.class) {
            if (init) {
                return;
            }
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            this.setContext(lc);
            this.setName("monitorAppender");
            this.start();
            ch.qos.logback.classic.Logger logger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
            logger.addAppender(this);
            init = true;
        }
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        Level level = eventObject.getLevel();
        if (level == Level.ERROR) {
            handleError(eventObject);
        } else if (level == Level.WARN) {
            handleWarn(eventObject);
        }
    }

    private void handleWarn(ILoggingEvent eventObject) {
        try {
            logAlarmHandler.handleWarn(eventObject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void handleError(ILoggingEvent eventObject) {
        try {
            logAlarmHandler.handleError(eventObject);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
