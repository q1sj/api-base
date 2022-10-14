package com.xsy.file.config;


import com.xsy.base.util.SpringContextUtils;
import com.xsy.file.service.FileStorageStrategy;
import com.xsy.file.service.LocalFileStorageStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 如果采用本地文件存储策略,此配置类生效
 * 文件访问地址为 http://ip:port/api/static/{@link com.xsy.file.entity.FileRecordEntity#getPath()}
 * 其他分布式存储策略考虑使用nginx代理/api/static实现
 *
 * @author Q1sj
 */
@Configuration
public class LocalFileConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileStorageStrategy fileStorageStrategy = SpringContextUtils.getBean(FileStorageStrategy.class);
        // @Bean配置 @ConditionalOnBean(LocalFileStorageStrategy.class)无效
        if (!(fileStorageStrategy instanceof LocalFileStorageStrategy)) {
            return;
        }
        String basePath = ((LocalFileStorageStrategy) fileStorageStrategy).getBasePath();
        String suffix = "/";
        if (!basePath.endsWith(suffix)) {
            basePath += suffix;
        }
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + basePath);
    }
}
