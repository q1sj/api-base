package com.xsy.file.config;

import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Q1sj
 * @date 2022.11.22 9:15
 */
@Data
@Component
@ConfigurationProperties(prefix = FileStorageProperties.PREFIX)
public class FileStorageProperties {
    public static final String PREFIX = "file-storage";
    public static final String FAST_DFS_ENABLE = PREFIX + ".fastdfs.enable";

    private Local local = new Local();
    private FastDfs fastdfs = new FastDfs();


    @Data
    public static class FastDfs {
        private Boolean enable = false;
        private List<String> trackerList = new ArrayList<>();
    }

    @Data
    public static class Local {
        /**
         * 存储基础路径
         */
        private String basePath = "/tmp";
    }
}
