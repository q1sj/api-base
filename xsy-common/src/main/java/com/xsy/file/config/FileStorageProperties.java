package com.xsy.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
    public static final String MINIO_ENABLE = PREFIX + ".minio.enable";

    private Local local = new Local();
    private Minio minio = new Minio();
    private FastDfs fastdfs = new FastDfs();


    @Data
    public static class FastDfs {
        /**
         * 是否启用
         * 依赖存在 且 enable=true则使用fastdfs存储
         * <pre>{@code
         * <dependency>
         *      <groupId>com.github.tobato</groupId>
         *      <artifactId>fastdfs-client</artifactId>
         *      <version>${fastdfs-client.version}</version>
         * </dependency>
         * }</pre>
         */
        private Boolean enable = true;
    }

    @Data
    public static class Minio {
        /**
         * 是否启用
         * 依赖存在 且 enable=true则使用minio存储
         * <pre>{@code
         * <dependency>
         *     <groupId>io.minio</groupId>
         *     <artifactId>minio</artifactId>
         *     <version>8.5.10</version>
         * </dependency>
         * }</pre>
         */
        private Boolean enable;
        private String endpoint;
        private String accessKey;
        private String secretKey;
        private String bucketName;
    }

    @Data
    public static class Local {
        /**
         * 存储基础路径
         */
        private String basePath = System.getProperties().getProperty("user.home").replace("\\", "/");
    }
}
