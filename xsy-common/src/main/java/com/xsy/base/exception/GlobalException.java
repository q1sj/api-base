/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.base.exception;


import com.xsy.base.enums.ResultCodeEnum;

/**
 * 自定义异常
 *
 * @author Mark sunlightcs@gmail.com
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final ResultCodeEnum ERROR_CODE = ResultCodeEnum.INTERNAL_SERVER_ERROR;

    private final ResultCodeEnum code;

    public GlobalException(String msg) {
        super(msg);
        this.code = ERROR_CODE;
    }

    public GlobalException(ResultCodeEnum resultCodeEnum, String msg) {
        super(msg);
        this.code = resultCodeEnum;
    }

    public GlobalException(String msg, Throwable e) {
        super(msg, e);
        this.code = ERROR_CODE;
    }

    public GlobalException(ResultCodeEnum resultCodeEnum, String msg, Throwable cause) {
        super(msg, cause);
        this.code = resultCodeEnum;
    }

    public ResultCodeEnum getCode() {
        return code;
    }
}
