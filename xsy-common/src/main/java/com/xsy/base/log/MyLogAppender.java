package com.xsy.base.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author Q1sj
 * @date 2022.4.29 9:18
 */
@Slf4j
public class MyLogAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private static final MyLogAppender INSTANCE = new MyLogAppender();

    private boolean init = false;

    private MyLogAppender(){}

    public static MyLogAppender getInstance(){
        return INSTANCE;
    }

    @PostConstruct
    public void init() {
        if (init) {
            return;
        }
        synchronized (MyLogAppender.class) {
            if (init) {
                return;
            }
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            this.setContext(lc);
            this.setName("myLogAppender");
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

    protected void handleWarn(ILoggingEvent eventObject) {
        log.debug("TODO handleWarn");
    }

    protected void handleError(ILoggingEvent eventObject) {
        log.debug("TODO handleError");
    }
}
