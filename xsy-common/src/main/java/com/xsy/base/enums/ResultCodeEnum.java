package com.xsy.base.enums;

import lombok.AllArgsConstructor;

/**
 * 返回状态码
 * 错误码以不断追加的方式进行兼容。错误等级由日志和错误码本身的释义来决定。
 * 错误码使用者避免随意定义新的错误码。
 * 尽可能在原有错误码附表中找到语义相同或者相近的错误码在代码中使用即可。
 *
 * @author Q1sj
 * @date 2022.10.20 15:40
 */
@AllArgsConstructor
public enum ResultCodeEnum implements BaseEnum<Integer, String> {
    // 默认
    SUCCESS(0, "操作成功"),

	// 客户端错误 4xxxx
	CLIENT_ERROR(40000, "用户端错误"),
	// 输入错误 401xx;
	NOT_NULL(40101, "不能为空"),
	PARAMETER_VALIDATION_FAILED(40102, "参数校验失败"),
	RECORD_EXISTS(40103, "记录已存在"),
	// 认证失败 403xx
	AUTHENTICATE_FAIL(40300, "认证失败"),
	NO_LOGIN(40301, "未登录"),
	LOGIN_EXPIRED(40302, "用户登陆已过期"),
	// 授权失败
	UNAUTHORIZED(40303, "授权失败"),
    // 第三方服务错误 3xxxx,
    THIRD_PARTY_SERVICES_ERROR(30000, "调用第三方服务出错"),
    // 服务内部错误 5xxxx,
    INTERNAL_SERVER_ERROR(50000, "服务内部错误"),
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
