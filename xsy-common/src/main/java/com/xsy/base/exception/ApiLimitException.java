package com.xsy.base.exception;

import com.xsy.base.enums.ResultCodeEnum;

/**
 * @author Q1sj
 * @date 2024/5/28 上午10:46
 */
public class ApiLimitException extends GlobalException {

	private final String key;

	public ApiLimitException(String key, String msg) {
		super(ResultCodeEnum.CLIENT_ERROR, msg);
		this.key = key;
	}

	public ApiLimitException(String key, String msg, Throwable e) {
		super(ResultCodeEnum.CLIENT_ERROR, msg, e);
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
