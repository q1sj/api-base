/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.base.exception;

import com.xsy.base.util.Result;
import com.xsy.security.user.SecurityUser;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(ParamValidationException.class)
    public Result<?> handleParamValidationException(ParamValidationException e) {
        logger.warn(e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(GlobalException.class)
    public Result<?> handleRenException(GlobalException ex) {
        logger.error(ex.getMessage(), ex);
        return Result.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Result<?> handleDuplicateKeyException(DuplicateKeyException ex) {
        logger.warn(ex.getMessage(), ex);
        return Result.error("数据库已存在");
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleAuthorizationException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        return getResult(e, bindingResult);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleAuthorizationException(MissingServletRequestParameterException e) {
        logger.warn(e.getMessage(), e);
        return Result.error("缺少参数:" + e.getParameterName());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleException(DataIntegrityViolationException e) {
        logger.warn(e.getMessage(), e);
        return Result.error("参数校验失败");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleAuthorizationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return getResult(e, bindingResult);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public Result<?> handleException(UnauthorizedException ex) {
        logger.warn("userId:{} {}", SecurityUser.getUserId(), ex.getMessage());
        return Result.error("没有权限");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleException(HttpMessageNotReadableException ex) {
        logger.warn(ex.getMessage(), ex);
        return Result.error("参数解析失败");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<?> handleException(HttpRequestMethodNotSupportedException ex) {
        logger.warn(ex.getMessage());
        return Result.error("不支持的请求方式");
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
        return Result.error(ex);
    }

    private Result<?> getResult(Exception e, BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder("校验失败:");
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField()).append(fieldError.getDefaultMessage()).append(" ");
        }
        logger.warn(errorMessage.toString(), e);
        return Result.error(errorMessage.toString());
    }
}
