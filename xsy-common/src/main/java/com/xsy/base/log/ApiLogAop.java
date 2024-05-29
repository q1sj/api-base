package com.xsy.base.log;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.xsy.base.util.IpUtils;
import com.xsy.base.util.JsonUtils;
import com.xsy.base.util.StringUtils;
import com.xsy.security.user.SecurityUser;
import com.xsy.sys.entity.SysLogEntity;
import com.xsy.sys.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;


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
    @Value("${api.log.save-db:true}")
    private Boolean logSaveDb;
    @Autowired
    private SysLogService sysLogService;

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
        Throwable throwable = null;
        String argsStr = getLogJson(point.getArgs());
        try {
            resp = point.proceed();
            return resp;
        } catch (Throwable t) {
            throwable = t;
            throw t;
        } finally {
            String method = getMethodName(point.getSignature());
            String ip = request != null ? IpUtils.getIpAddr(request) : "null";
            String username = SecurityUser.getUser().getUsername();
            String url = request != null ? getRequestURL() : "null";
            long cost = System.currentTimeMillis() - startTime;
            String respLogJson = getLogJson(resp);
            log.info("method:{} ip:{} user:{} url:{} args:{} resp:{} throwable:{} cost:{}ms",
                    method, ip, username, url, argsStr, respLogJson, throwable, cost);
            if (logSaveDb) {
                try {
                    SysLogEntity entity = new SysLogEntity();
                    entity.setId(IdWorker.getId());
                    entity.setMethod(method);
                    entity.setIp(ip);
                    entity.setUsername(username);
                    entity.setUrl(url);
                    entity.setArgs(argsStr);
                    entity.setResp(respLogJson);
                    entity.setThrowable(Objects.toString(throwable));
                    entity.setCost(cost);
                    entity.setRecordTime(new Date());
                    entity.setCreateTime(new Date());
                    sysLogService.save(entity);
                } catch (Exception e) {
                    log.error("系统日志落库失败 {}", e.getMessage(), e);
                }
            }
        }
    }

    private String getRequestURL() {
        try {
            return Objects.toString(request.getRequestURL());
        } catch (Exception e) {
            log.warn("url获取失败 {}", e.getMessage());
            return "null";
        }
    }

    /**
     * 存在{@link ApiLog}注解时，获取注解的method 默认为方法名
     *
     * @param signature
     * @return
     */
    private String getMethodName(Signature signature) {
        if (signature instanceof MethodSignature) {
            Method method = ((MethodSignature) signature).getMethod();
            ApiLog apiLogAnnotation = method.getAnnotation(ApiLog.class);
            if (apiLogAnnotation != null && StringUtils.isNotBlank(apiLogAnnotation.methodName())) {
                return apiLogAnnotation.methodName();
            }
        }
        return Objects.toString(signature);
    }

    private static String getLogJson(Object obj) {
        if (obj == null) {
            return "";
        }
        if (String.class.equals(obj.getClass())) {
            return obj.toString();
        }
        try {
            return JsonUtils.toLogJsonString(obj);
        } catch (Exception e) {
            return Objects.toString(obj);
        }
    }
}
