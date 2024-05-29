package com.xsy.base.exception;

import com.xsy.base.enums.ResultCodeEnum;

/**
 * @author Q1sj
 * @date 2024.4.22 10:35
 */
public class RequestTimeoutException extends GlobalException {
	public RequestTimeoutException(String msg) {
		super(msg);
	}

	public RequestTimeoutException(String msg, Throwable e) {
		super(ResultCodeEnum.THIRD_PARTY_SERVICES_ERROR, msg, e);
	}
}
