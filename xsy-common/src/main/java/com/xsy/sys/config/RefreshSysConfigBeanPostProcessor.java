package com.xsy.sys.config;

import com.xsy.base.util.CollectionUtils;
import com.xsy.sys.annotation.SysConfig;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Q1sj
 * @date 2023.3.24 16:40
 */
@Component
@Slf4j
public class RefreshSysConfigBeanPostProcessor implements RefreshConfigEventListener, BeanPostProcessor, Ordered {
	private static final Map<String, List<SysConfigField>> SYS_CONFIG_FIELD_MAP = new ConcurrentHashMap<>();
	/**
	 * key:参数管理key
	 * val:该key依赖的key,依赖的key更新时该key也需要更新
	 */
	private static final Map<String, List<String>> DEPEND_SYS_CONFIG_KEY = new ConcurrentHashMap<>();
	private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}", ":", false);

	@Autowired
	@Lazy
	private ConversionService mvcConversionService;
	@Autowired
	@Lazy
	private SysConfigService sysConfigService;

	public Set<String> sysKeySet() {
		return new HashSet<>(SYS_CONFIG_FIELD_MAP.keySet());
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		initSysConfigField(bean);
		return bean;
	}

	@Override
	public void refreshConfigEvent(String key) {
		List<SysConfigField> sysConfigFields = SYS_CONFIG_FIELD_MAP.get(key);
		if (CollectionUtils.isEmpty(sysConfigFields)) {
			return;
		}
		log.info("refreshConfigEvent key:{}", key);
		String valueStr = sysConfigService.get(key);
		sysConfigFields.forEach(sysConfigField -> refreshValue(sysConfigField, valueStr));
		// 更新value依赖项
		updateValueDepend(key);
		// 更新依赖此key的key
		for (Map.Entry<String, List<String>> entry : DEPEND_SYS_CONFIG_KEY.entrySet()) {
			if (CollectionUtils.contains(entry.getValue().listIterator(), key)) {
				refreshConfigEvent(entry.getKey());
			}
		}
	}

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

	private void initSysConfigField(Object bean) throws BeansException {
		Class<?> beanClass = bean.getClass();
		List<Field> fields = getAllFields(beanClass);
		for (Field field : fields) {
			SysConfig sysConfig = AnnotatedElementUtils.findMergedAnnotation(field, SysConfig.class);
			if (sysConfig == null) {
				continue;
			}
			String sysConfigKey = sysConfig.value();
			if (StringUtils.isBlank(sysConfigKey)) {
				sysConfigKey = field.getName();
			}
			SysConfigField sysConfigField = new SysConfigField(sysConfigKey, bean, field);
			List<SysConfigField> sysConfigFieldList = SYS_CONFIG_FIELD_MAP.computeIfAbsent(sysConfigKey, key -> new CopyOnWriteArrayList<>());
			if (!sysConfigFieldList.isEmpty() && !sysConfigFieldList.get(0).field.getType().equals(field.getType())) {
				throw new BeanInitializationException("同一个key只允许一种类型," + sysConfigKey + "类型不同" + sysConfigFieldList.get(0).field.getType() + " " + field.getType());
			}
			sysConfigFieldList.add(sysConfigField);
			refreshValue(sysConfigField);
			// 更新value依赖项
			updateValueDepend(sysConfigKey);
		}
	}

	private void updateValueDepend(String key) {
		DEPEND_SYS_CONFIG_KEY.remove(key);
		String valueStr = sysConfigService.getOriginal(key);
		if (valueStr == null) {
			return;
		}
		propertyPlaceholderHelper.replacePlaceholders(valueStr, new PropertyPlaceholderHelper.PlaceholderResolver() {
			@Override
			public String resolvePlaceholder(String placeholderName) {
				DEPEND_SYS_CONFIG_KEY.compute(key, (k, v) -> {
					if (v == null) {
						v = new ArrayList<>();
					}
					v.add(placeholderName);
					return v;
				});
				return "";
			}
		});
	}

	private List<Field> getAllFields(Class<?> beanClass) {
		ArrayList<Field> fields = new ArrayList<>(Arrays.asList(beanClass.getDeclaredFields()));
		Class<?> superclass = beanClass.getSuperclass();
		while (superclass != null && superclass != Object.class) {
			fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
			superclass = superclass.getSuperclass();
		}
		return fields;
	}


	private void refreshValue(SysConfigField sysConfigField, String valueStr) {
		if (valueStr == null) {
			log.info("{}不存在,使用原始值 {}", sysConfigField.key, sysConfigField.getValue());
			return;
		}
		Object newValue;
		try {
			newValue = mvcConversionService.convert(valueStr, sysConfigField.field.getType());
		} catch (Exception e) {
			throw new IllegalArgumentException(sysConfigField.key + "不合法", e);
		}
		try {
			sysConfigField.setValue(newValue);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		log.info("key:{} field:{} 设置为:{}", sysConfigField.key, sysConfigField.field, newValue);
	}

	private void refreshValue(SysConfigField sysConfigField) {
		String valueStr = sysConfigService.get(sysConfigField.key);
		refreshValue(sysConfigField, valueStr);
	}

	static class SysConfigField {
		String key;
		Object bean;
		Field field;

		public SysConfigField(String key, Object bean, Field field) {
			this.key = key;
			this.bean = bean;
			this.field = field;
		}

		public void setValue(Object value) throws IllegalAccessException {
			field.setAccessible(true);
			field.set(bean, value);
		}

		@Nullable
		public Object getValue() {
			field.setAccessible(true);
            try {
                return field.get(bean);
            } catch (IllegalAccessException e) {
                log.warn(e.getMessage(), e);
                return null;
            }
        }
    }
}
