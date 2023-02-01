package com.xsy.api.common.config;

import com.xsy.security.config.BaseAuthFilterMapConfig;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Q1sj
 * @date 2022.8.30 14:23
 */
@Component
public class AuthFilterMapConfig extends BaseAuthFilterMapConfig {

    private final Map<String, String> filterMap = new LinkedHashMap<>();

    {
        // smart-doc
        filterMap.put("/doc/**", DefaultFilter.anon.name());
        filterMap.put("/actuator/**", DefaultFilter.anon.name());
    }

    @Override
    public Map<String, String> getFilterMap() {
        return filterMap;
    }
}
