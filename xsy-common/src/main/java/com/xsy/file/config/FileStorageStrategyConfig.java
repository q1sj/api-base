package com.xsy.file.config;

import com.xsy.file.service.FileStorageStrategy;
import com.xsy.file.service.LocalFileStorageStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储策略配置
 *
 * @author Q1sj
 * @date 2022.10.14 10:32
 */
@Configuration
public class FileStorageStrategyConfig {
    @Bean
    public FileStorageStrategy fileStorageStrategy() {
        // 暂支持持本地存储
        return new LocalFileStorageStrategy();
    }
}
