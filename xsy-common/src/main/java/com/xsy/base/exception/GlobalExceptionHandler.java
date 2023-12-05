/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.base.exception;

import ch.qos.logback.core.util.FileSize;
import com.xsy.base.enums.ResultCodeEnum;
import com.xsy.base.util.Result;
import com.xsy.security.user.SecurityUser;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;


/**
 * 异常处理器
 */
@Order(0)
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ParamValidationException.class)
    public Result<?> handleParamValidationException(ParamValidationException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED, e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(GlobalException.class)
    public Result<?> handleException(GlobalException ex) {
        logger.error(ex.getMessage(), ex);
        return Result.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException ex) {
        logger.warn(ex.getMessage(), ex);
        return Result.error(ResultCodeEnum.RECORD_EXISTS);
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return getResult(e, bindingResult);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result<?> handleException(MaxUploadSizeExceededException e) {
        logger.error(e.getMessage(), e);
        if (e.getMaxUploadSize() > 0) {
            return Result.error("文件上传大小不能大于" + new FileSize(e.getMaxUploadSize()));
        }
        return Result.error("上传文件过大");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleException(MissingServletRequestParameterException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED, "缺少参数:" + e.getParameterName());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public Result<?> handleException(MissingRequestHeaderException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED, "缺少header参数:" + e.getHeaderName());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public Result<?> handleException(MissingPathVariableException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED, "缺少pathVariable:" + e.getVariableName());
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public Result<?> handleException(MissingRequestCookieException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED, "缺少cookie:" + e.getCookieName());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleException(DataIntegrityViolationException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleAuthorizationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return getResult(e, bindingResult);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result<?> handleException(UnauthorizedException ex) {
        logger.warn("userId:{} {}", SecurityUser.getUserId(), ex.getMessage());
        return Result.error(ResultCodeEnum.UNAUTHORIZED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleException(HttpMessageNotReadableException ex) {
        logger.warn(ex.getMessage(), ex);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleException(HttpRequestMethodNotSupportedException ex) {
        logger.warn(ex.getMessage());
        return Result.error(ResultCodeEnum.CLIENT_ERROR, "不支持的请求方式");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleException(IllegalArgumentException ex) {
        logger.warn(ex.getMessage(), ex);
        return Result.error(ResultCodeEnum.CLIENT_ERROR, "非法参数 " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return Result.error(ex);
    }

    private Result<?> getResult(Exception e, BindingResult bindingResult) {
        List<String> errorMessages = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessages.add(fieldError.getField() + fieldError.getDefaultMessage());
        }
        String errorMessage = "校验失败:" + String.join(",", errorMessages);
        logger.warn(errorMessage, e);
        return Result.error(ResultCodeEnum.PARAMETER_VALIDATION_FAILED, errorMessage);
    }
}
