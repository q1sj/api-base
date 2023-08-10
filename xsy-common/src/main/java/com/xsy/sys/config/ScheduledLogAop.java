package com.xsy.sys.config;

import com.xsy.job.entity.ScheduleJobLogEntity;
import com.xsy.job.service.ScheduleJobLogService;
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
	private ScheduleJobLogService scheduleJobLogService;

	@Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long startTime = System.currentTimeMillis();
		String taskName = getTaskName(point);
		log.info("定时任务{} 运行开始", taskName);
		ScheduleJobLogEntity logEntity = new ScheduleJobLogEntity();

		logEntity.setJobId(Integer.toUnsignedLong(taskName.hashCode()));
		logEntity.setBeanName(taskName);
		logEntity.setParams("");
		logEntity.setCreateTime(new Date());
		logEntity.setStatus(ScheduleJobLogEntity.EXECUTION_STATUS);
		logEntity.setTimes(-1);
		scheduleJobLogService.save(logEntity);
		try {
			Object proceed = point.proceed();
			logEntity.setStatus(ScheduleJobLogEntity.SUCCESS_STATUS);
			return proceed;
		} catch (Throwable t) {
			logEntity.setStatus(ScheduleJobLogEntity.FAIL_STATUS);
			logEntity.setError(t.getMessage());
			log.error("定时任务{} 运行失败 exception:{}", taskName, t.toString());
			throw t;
		} finally {
			long cost = System.currentTimeMillis() - startTime;
			log.info("定时任务{} 运行结束 耗时:{}ms", taskName, cost);
			logEntity.setTimes((int) cost);
			scheduleJobLogService.saveOrUpdate(logEntity);
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
