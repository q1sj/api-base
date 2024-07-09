package com.xsy.sys.config;

import com.xsy.sys.entity.RefreshConfigEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;

/**
 * @author Q1sj
 * @date 2023.5.12 16:39
 */
public interface RefreshConfigEventListener extends ApplicationListener<RefreshConfigEvent>, Ordered {

    void refreshConfigEvent(String key, String value);

    default void onApplicationEvent(RefreshConfigEvent event) {
        refreshConfigEvent(event.getKey(), event.getValue());
    }

    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
