package com.xsy.security.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2024/5/27 下午4:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ApiLimit {
	/**
	 * 资源的key,唯一
	 * 作用：不同的接口，不同的流量控制
	 */
	String key() default "";

	/**
	 * 最多的访问限制次数
	 */
	int rate();

	/**
	 * 间隔时间 毫秒
	 */
	long rateIntervalMillisecond() default 1000;

	/**
	 * 获取令牌最大等待时间
	 */
	long timeout() default 0;

	/**
	 * 获取令牌最大等待时间,单位(例:分钟/秒/毫秒) 默认:毫秒
	 */
	TimeUnit timeunit() default TimeUnit.SECONDS;

	/**
	 * 得不到令牌的提示语
	 */
	String msg() default "系统繁忙,请稍后再试";
}
