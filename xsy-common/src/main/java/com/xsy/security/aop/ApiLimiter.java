package com.xsy.security.aop;

import com.xsy.base.exception.ApiLimitException;
import com.xsy.security.annotation.ApiLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Q1sj
 * @date 2024/5/27 下午4:49
 */
@Slf4j
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ApiLimiter {
	/**
	 * 不同的接口，不同的流量控制
	 * map的key为 @ApiLimit.key
	 */
	private final Map<String, RRateLimiter> limitMap = new ConcurrentHashMap<>();

	@Autowired
	private RedissonClient redissonClient;

	@Pointcut("@annotation(com.xsy.security.annotation.ApiLimit)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		//拿limit的注解
		ApiLimit limit = method.getAnnotation(ApiLimit.class);
		if (limit == null) {
			return joinPoint.proceed();
		}
		//key作用：不同的接口，不同的流量控制
		String key = limit.key();
		RRateLimiter rateLimiter = limitMap.computeIfAbsent(key, k -> {
			RRateLimiter limiter = redissonClient.getRateLimiter("rateLimiter::" + k);
			limiter.setRate(RateType.OVERALL, limit.rate(), limit.rateIntervalMillisecond(), RateIntervalUnit.MILLISECONDS);
			return limiter;
		});
		// 拿令牌
		boolean acquire = rateLimiter.tryAcquire(limit.timeout(), limit.timeunit());
		// 拿不到命令，直接返回异常提示
		if (!acquire) {
			throw new ApiLimitException(key, limit.msg());
		}
		return joinPoint.proceed();
	}
}
