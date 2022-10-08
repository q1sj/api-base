/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.base.exception;


import java.util.Arrays;

/**
 * 自定义异常
 *
 * @author Mark sunlightcs@gmail.com
 */
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public static final int ERROR_CODE = 500;

    private int code;

    public GlobalException(String msg) {
        super(msg);
        this.code = ERROR_CODE;
    }

    public GlobalException(String msg, Throwable e) {
        super(msg, e);
        this.code = ERROR_CODE;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        StackTraceElement[] stackTrace = super.getStackTrace();
        return Arrays.stream(stackTrace).filter(stackTraceElement -> stackTraceElement.getClassName().startsWith("com.xsy")).toArray(StackTraceElement[]::new);
    }
}
