package com.xsy.sys.config;

import com.xsy.sys.entity.SysTaskLogEntity;
import com.xsy.sys.service.SysTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Q1sj
 * @date 2023.6.5 14:06
 */
@Slf4j
@Aspect
@Component
public class ScheduledLogAop {
	@Autowired
	private SysTaskLogService sysTaskLogService;

	@Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long startTime = System.currentTimeMillis();
		String taskName = getTaskName(point);
		log.info("定时任务{} 运行开始", taskName);
		SysTaskLogEntity logEntity = new SysTaskLogEntity();
		logEntity.setTaskId((long) taskName.hashCode());
		logEntity.setTaskName(taskName);
		logEntity.setCreateTime(new Date());
		logEntity.setStatus(SysTaskLogEntity.RUNNING_STATUS);
		logEntity.setCost(-1);
		logEntity.setMsg("");
		sysTaskLogService.save(logEntity);
		try {
			return point.proceed();
		} catch (Throwable t) {
			logEntity.setStatus(SysTaskLogEntity.FAIL_STATUS);
			logEntity.setMsg(t.toString());
			log.error("定时任务{} 运行失败 exception:{}", taskName, t.toString());
			throw t;
		} finally {
			long cost = System.currentTimeMillis() - startTime;
			log.info("定时任务{} 运行结束 耗时:{}ms", taskName, cost);
			logEntity.setCost((int) cost);
			sysTaskLogService.saveOrUpdate(logEntity);
		}
	}

	private static String getTaskName(ProceedingJoinPoint point) {
		try {
			Signature signature = point.getSignature();
			return signature.getDeclaringType().getName() + "#" + signature.getName();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "unknown";
		}
	}
}
