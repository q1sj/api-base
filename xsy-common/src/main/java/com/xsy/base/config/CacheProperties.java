package com.xsy.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Q1sj
 * @date 2023.6.12 15:00
 */
@Data
@Configuration
@ConfigurationProperties(prefix = CacheProperties.PREFIX_PROPERTIES_NAME)
public class CacheProperties {
	public static final String PREFIX_PROPERTIES_NAME = "cache";
	public static final String ENABLE_PROPERTIES_NAME = PREFIX_PROPERTIES_NAME + ".enable";

	/**
	 * 是否启用
	 */
	private Boolean enable = true;
	/**
	 * 缓存过期时间
	 */
	private Integer expire = 1;
	/**
	 * 缓存过期时间单位
	 */
	private String expireTimeUnit = "HOURS";
	/**
	 * 缓存最大大小
	 */
	private Long cacheMaxSize = 10000L;
}
