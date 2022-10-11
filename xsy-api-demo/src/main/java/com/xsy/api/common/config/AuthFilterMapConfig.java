package com.xsy.api.common.config;

import com.xsy.security.config.BaseAuthFilterMapConfig;
import org.springframework.stereotype.Component;

/**
 * @author Q1sj
 * @date 2022.8.30 14:23
 */
@Component
public class AuthFilterMapConfig extends BaseAuthFilterMapConfig {
    {
        // smart-doc
        filterMap.put("/doc/**", "anon");
    }
}
