package com.xsy.base.exception;

import com.xsy.base.enums.ErrorCodeEnum;

/**
 * 参数校验异常
 *
 * @author Q1sj
 * @date 2022.9.27 16:21
 */
public class ParamValidationException extends GlobalException {
    public ParamValidationException(String msg) {
        super(ErrorCodeEnum.PARAMETER_VALIDATION_FAILED, msg);
    }

    public ParamValidationException(String msg, Throwable e) {
        super(ErrorCodeEnum.PARAMETER_VALIDATION_FAILED, msg, e);
    }
}
