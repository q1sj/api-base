package com.xsy.base.controller;

import com.xsy.base.util.Result;
import com.xsy.security.annotation.NoAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用信息
 *
 * @author Q1sj
 * @date 2022.8.30 15:13
 */
@RestController
public class AppInfoController {
    public static final String VERSION_MAPPING = "/version";

    private final String applicationName;
    private final String version;

    private final long startTime;

    public AppInfoController(@Value("${spring.application.name:unknown}") String applicationName, @Value("${app.version:unknown}") String version) {
        this.applicationName = applicationName;
        this.version = version;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 系统版本号
     *
     * @return
     */
    @NoAuth
    @GetMapping(AppInfoController.VERSION_MAPPING)
    public Result<AppInfoVO> version() {
        return Result.ok(new AppInfoVO(applicationName, version, DurationFormatUtils.formatDuration(System.currentTimeMillis() - startTime, "dd天HH时mm分ss秒")));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class AppInfoVO {
        /**
         * 应用名称
         */
        private String applicationName;
        /**
         * 版本号
         */
        private String version;
        /**
         * 运行时间
         */
        private String uptime;
    }
}
