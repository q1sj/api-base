package com.xsy.base.enums;

import lombok.AllArgsConstructor;

/**
 * 错误码
 * 错误码以不断追加的方式进行兼容。错误等级由日志和错误码本身的释义来决定。
 * 错误码使用者避免随意定义新的错误码。
 * 尽可能在原有错误码附表中找到语义相同或者相近的错误码在代码中使用即可。
 *
 * @author Q1sj
 * @date 2022.10.20 15:40
 */
@AllArgsConstructor
public enum ErrorCodeEnum implements BaseEnum<Integer, String> {
    // 默认
    SUCCESS(0, "成功"),

    // 客户端错误 1xxxx
    CLIENT_ERROR(10000, "用户端错误"),
    // 输入错误 101xx;
    NOT_NULL(10101, "不能为空"),
    PARAMETER_VALIDATION_FAILED(10102, "参数校验失败"),
    RECORD_EXISTS(10103, "记录已存在"),
    // 认证失败 104xx
    AUTHENTICATE_FAIL(10400, "认证失败"),
    NO_LOGIN(10401, "未登录"),
    LOGIN_EXPIRED(10402, "用户登陆已过期"),
    // 授权失败 105xx
    UNAUTHORIZED(10500, "授权失败"),
    // 服务内部错误 2xxxx,
    INTERNAL_SERVER_ERROR(20000, "服务内部错误"),
    // 第三方服务错误 3xxxx,
    THIRD_PARTY_SERVICES_ERROR(30000, "调用第三方服务出错"),
    ;
    public final int code;
    public final String message;

    @Override
    public Integer getValue() {
        return code;
    }

    @Override
    public String getDesc() {
        return message;
    }
}
