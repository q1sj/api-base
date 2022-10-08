package com.xsy.base.exception;

/**
 * 参数校验异常
 *
 * @author Q1sj
 * @date 2022.9.27 16:21
 */
public class ParamValidationException extends GlobalException {
    public ParamValidationException(String msg) {
        super(msg);
    }

    public ParamValidationException(String msg, Throwable e) {
        super(msg, e);
    }
}
