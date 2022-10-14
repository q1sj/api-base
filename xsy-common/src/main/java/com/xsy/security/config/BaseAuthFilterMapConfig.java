package com.xsy.security.config;

import com.xsy.base.controller.AppInfoController;
import com.xsy.security.controller.LoginController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Q1sj
 * @date 2022.8.30 14:38
 */
public class BaseAuthFilterMapConfig {
    protected final Map<String, String> filterMap;

    {
        filterMap = new LinkedHashMap<>();
        filterMap.put(LoginController.LOGIN_MAPPING, "anon");
        filterMap.put(AppInfoController.VERSION_MAPPING, "anon");
    }

    public Map<String, String> getFilterMap() {
        return new LinkedHashMap<>(filterMap);
    }
}
