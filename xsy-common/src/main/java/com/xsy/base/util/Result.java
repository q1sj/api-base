package com.xsy.base.util;

import lombok.Data;

import java.util.Objects;

/**
 * @author Q1sj
 * @date 2021.11.11 10:50
 */
@Data
public class Result<T> {
    /**
     * 成功code
     */
    private static final int SUCCESS_CODE = 0;
    private static final String SUCCESS_MSG = "操作成功";
    /**
     * 默认失败code
     */
    private static final int FAIL_CODE = 500;
    /**
     * 默认失败msg
     */
    private static final String FAIL_MSG = "未知异常,请联系管理员";
    /**
     * code 0 成功 其他失败
     */
    private Integer code;
    /**
     * 用户的提示信息
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
    /**
     * 后端异常信息
     */
    private String exception;
    /**
     * 响应时间戳
     */
    private Long timestamp;

    public Result() {
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> Result<T> ok() {
        return new Result<T>(SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> Result<T> ok(T data) {
        return Result.<T>ok().setData(data);
    }

    public static <T> Result<T> error() {
        return new Result<T>(FAIL_CODE, FAIL_MSG);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<T>(FAIL_CODE, msg);
    }

    public static <T> Result<T> error(Exception e) {
        Objects.requireNonNull(e);
        Result<T> result = new Result<>(FAIL_CODE, FAIL_MSG);
        result.setException(e.getClass().getName());
        return result;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isSuccess() {
        return this.code == SUCCESS_CODE;
    }

    @Deprecated
    public static <T> Result<T> error(int code) {
        return new Result<>(code, FAIL_MSG);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message);
    }
}
