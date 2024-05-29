package com.xsy.security.config;

import com.xsy.security.aop.ApiLimiter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Q1sj
 * @date 2024/5/28 下午2:50
 */
@Configuration
public class ApiLimiterConfig {

	@Bean
	@ConditionalOnClass(name = "org.redisson.api.RedissonClient")
	public ApiLimiter apiLimiter() {
		return new ApiLimiter();
	}
}
