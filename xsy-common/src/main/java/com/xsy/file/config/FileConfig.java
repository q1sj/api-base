package com.xsy.file.config;

import com.xsy.file.service.FileStorageStrategy;
import com.xsy.file.service.LocalFileStorageStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Q1sj
 * @date 2022.8.30 15:49
 */
@Component
@Configuration
public class FileConfig {

    @Bean
    public FileStorageStrategy fileStorageStrategy() {
        return new LocalFileStorageStrategy();
    }
}
