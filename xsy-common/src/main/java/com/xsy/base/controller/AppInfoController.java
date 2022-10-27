package com.xsy.base.controller;

import com.xsy.base.util.Result;
import com.xsy.security.annotation.NoAuth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private final VersionVO versionVO;

    public AppInfoController(@Value("${app.version}") String version) {
        this.versionVO = new VersionVO(version);
    }

    /**
     * 系统版本号
     *
     * @return
     */
    @NoAuth
    @GetMapping(AppInfoController.VERSION_MAPPING)
    public Result<VersionVO> version() {
        return Result.ok(versionVO);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VersionVO {
        /**
         * 版本号
         */
        private String version;
    }
}
