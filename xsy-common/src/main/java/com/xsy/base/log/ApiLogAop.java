package com.xsy.base.log;

import com.xsy.base.util.IpUtils;
import com.xsy.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * post接口日志切面
 *
 * @author Q1sj
 * @date 2022.9.22 14:31
 */
@Slf4j
@Aspect
@Component
public class ApiLogAop {
    @Autowired(required = false)
    private HttpServletRequest request;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(com.xsy.base.log.ApiLog)")
    public void pointcut() {
    }

    @Pointcut("@annotation(com.xsy.base.log.IgnoreLog)")
    public void ignoreLog() {
    }

    @Around("pointcut() && !ignoreLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object resp = null;
        try {
            resp = point.proceed();
            return resp;
        } finally {
            if (log.isInfoEnabled()) {
                log.info("{} ip:{} user:{} url:{} args:{} resp:{} cost:{}ms", point.getSignature(), request != null ? IpUtils.getIpAddr(request) : "null", SecurityUser.getUserId(), request != null ? request.getRequestURL() : "null", Arrays.toString(point.getArgs()), resp, System.currentTimeMillis() - startTime);
            }
        }
    }
}
