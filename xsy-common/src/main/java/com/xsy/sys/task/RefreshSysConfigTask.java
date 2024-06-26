package com.xsy.sys.task;

import com.xsy.base.log.IgnoreLog;
import com.xsy.sys.config.RefreshConfigEventListener;
import com.xsy.sys.config.RefreshSysConfigBeanPostProcessor;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 参数管理定时刷新任务
 *
 * @author Q1sj
 * @date 2024/6/26 下午4:24
 */
@Slf4j
@Component
public class RefreshSysConfigTask implements RefreshConfigEventListener {
	private final Map<String, String> sysConfigMap = new ConcurrentHashMap<>();
	@Autowired
	private SysConfigService sysConfigService;
	@Autowired
	private RefreshSysConfigBeanPostProcessor refreshSysConfigBeanPostProcessor;

	@PostConstruct
	public void init() {
		Set<String> keySet = refreshSysConfigBeanPostProcessor.sysKeySet();
		for (String key : keySet) {
			String value = sysConfigService.get(key);
			if (value != null) {
				sysConfigMap.put(key, value);
			}
		}
	}

	@IgnoreLog
	@Scheduled(fixedDelay = 10 * 1000, initialDelay = 60 * 1000)
	public void run() {
		for (String key : refreshSysConfigBeanPostProcessor.sysKeySet()) {
			String newValue = sysConfigService.get(key);
			if (Objects.equals(newValue, sysConfigMap.get(key)) || newValue == null) {
				continue;
			}
			sysConfigMap.put(key, newValue);
			try {
				refreshSysConfigBeanPostProcessor.refreshConfigEvent(key);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public void refreshConfigEvent(String key) {
		sysConfigMap.put(key, sysConfigService.get(key));
	}
}
