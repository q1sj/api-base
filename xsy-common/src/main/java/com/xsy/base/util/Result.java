package com.xsy.base.util;

import com.xsy.base.enums.ResultCodeEnum;
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
    private static final int SUCCESS_CODE = ResultCodeEnum.SUCCESS.code;
    private static final String SUCCESS_MSG = ResultCodeEnum.SUCCESS.message;
    /**
     * 默认失败code
     */
    private static final int FAIL_CODE = ResultCodeEnum.INTERNAL_SERVER_ERROR.code;
    /**
     * 默认失败msg
     */
    private static final String FAIL_MSG = ResultCodeEnum.INTERNAL_SERVER_ERROR.message;
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

    public static <T> Result<T> error(ResultCodeEnum resultCodeEnum) {
        return new Result<>(resultCodeEnum.getValue(), resultCodeEnum.getDesc());
    }

    public static <T> Result<T> error(ResultCodeEnum resultCodeEnum, String userTip) {
        return new Result<>(resultCodeEnum.getValue(), userTip);
    }

    public static <T> Result<T> error(String userTip) {
        return new Result<T>(FAIL_CODE, userTip);
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
}
