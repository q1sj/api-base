package com.xsy.sys.entity;

import org.springframework.context.ApplicationEvent;


/**
 * @author Q1sj
 * @date 2023.3.24 16:39
 */
public class RefreshConfigEvent extends ApplicationEvent {

    public RefreshConfigEvent(String key) {
        super(key);
    }

    public String getKey() {
        return (String) source;
    }
}
