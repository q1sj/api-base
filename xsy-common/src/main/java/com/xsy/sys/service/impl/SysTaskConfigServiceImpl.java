package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.util.PageData;
import com.xsy.base.util.SpringContextUtils;
import com.xsy.base.util.StringUtils;
import com.xsy.sys.dao.SysTaskConfigDao;
import com.xsy.sys.dto.SysTaskConfigQuery;
import com.xsy.sys.entity.SysTaskConfigEntity;
import com.xsy.sys.service.DynamicTask;
import com.xsy.sys.service.SysTaskConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @author Q1sj
 * @date 2023.3.7 15:24
 */
@Slf4j
@Service
public class SysTaskConfigServiceImpl extends ServiceImpl<SysTaskConfigDao, SysTaskConfigEntity> implements SysTaskConfigService {

	@Autowired
	private DynamicTaskService dynamicTaskService;
	@Autowired
	@Lazy
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Override
	public PageData<SysTaskConfigEntity> page(SysTaskConfigQuery query) {
		LambdaQueryWrapper<SysTaskConfigEntity> wrapper = Wrappers.lambdaQuery(SysTaskConfigEntity.class);
		wrapper.like(StringUtils.isNotBlank(query.getTaskName()), SysTaskConfigEntity::getTaskName, query.getTaskName());
		IPage<SysTaskConfigEntity> page = getBaseMapper().selectPage(query.initPage(), wrapper);
		return new PageData<>(page);
	}

	@Override
	public void run(Integer id) {
		log.info("立即执行:{}", id);
		SysTaskConfigEntity taskConfig = getById(id);
		try {
			Object bean = SpringContextUtils.getBean(Class.forName(taskConfig.getTaskName()));
			if (bean instanceof DynamicTask) {
				threadPoolTaskScheduler.submit(() -> ((DynamicTask) bean).run(taskConfig.getParam()));
			}
		} catch (ClassNotFoundException e) {
			throw new GlobalException("task class不存在", e);
		}
	}

	@Override
	public SysTaskConfigEntity getById(Serializable id) {
		// TODO 添加业务逻辑
		return super.getById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(SysTaskConfigEntity entity) {
        if (!CronSequenceGenerator.isValidExpression(entity.getCronExpression())) {
            throw new GlobalException("cron表达式不合法");
        }
        boolean save = super.save(entity);
        if (save) {
            dynamicTaskService.add(entity.getTaskName());
        }
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(SysTaskConfigEntity entity) {
        boolean update = super.updateById(entity);
        if (update) {
            dynamicTaskService.add(entity.getTaskName());
        }
        return update;
    }

    @Override
    public boolean removeById(Serializable id) {
        SysTaskConfigEntity entity = getById(id);
        if (entity == null) {
            return false;
        }
        boolean remove = super.removeById(id);
        if (remove) {
	        dynamicTaskService.delete(entity.getTaskName());
        }
        return remove;
    }
}
