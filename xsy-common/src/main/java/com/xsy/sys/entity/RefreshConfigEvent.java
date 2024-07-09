package com.xsy.sys.entity;

import org.springframework.context.ApplicationEvent;


/**
 * @author Q1sj
 * @date 2023.3.24 16:39
 */
public class RefreshConfigEvent extends ApplicationEvent {

    public String value;

    public RefreshConfigEvent(String key, String value) {
        super(key);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getKey() {
        return (String) source;
    }
}
