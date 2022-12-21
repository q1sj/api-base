package com.xsy.file.config;

import com.xsy.base.util.BizAssertUtils;
import com.xsy.file.service.FastDfsFileStorageStrategy;
import com.xsy.file.service.FileStorageStrategy;
import com.xsy.file.service.LocalFileStorageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 文件存储策略配置
 *
 * @author Q1sj
 * @date 2022.10.14 10:32
 */
@Slf4j
@Configuration
public class FileStorageStrategyConfig {
    @Autowired
    private FileStorageProperties fileStorageProperties;

    @Bean
    @ConditionalOnClass(name = "com.github.tobato.fastdfs.service.FastFileStorageClient")
    @ConditionalOnProperty(name = FileStorageProperties.FAST_DFS_ENABLE, havingValue = "true",matchIfMissing = true)
    public FastDfsFileStorageStrategy fastDfsFileStorageStrategy() {
        log.info("init fastDfsFileStorageStrategy");
        //TODO
        return new FastDfsFileStorageStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(FileStorageStrategy.class)
    public LocalFileStorageStrategy localFileStorageStrategy() {
        log.info("init localFileStorageStrategy");
        FileStorageProperties.Local localStorage = fileStorageProperties.getLocal();
        LocalFileStorageStrategy localFileStorageStrategy = new LocalFileStorageStrategy();
        localFileStorageStrategy.setBasePath(localStorage.getBasePath());
        return localFileStorageStrategy;
    }
}
