package com.xsy.security.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Q1sj
 * @date 2022.8.30 14:38
 */
public class BaseAuthFilterMapConfig {
    protected final Map<String, String> filterMap = new LinkedHashMap<>();

    public Map<String, String> getFilterMap() {
        return filterMap;
    }
}
