package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.PageData;
import com.xsy.sys.dao.SysConfigDao;
import com.xsy.sys.entity.BaseKey;
import com.xsy.sys.entity.SysConfigEntity;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * @author Q1sj
 * @date 2022.9.20 14:12
 */
@Slf4j
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfigEntity> implements SysConfigService {

    private static final String CACHE_NAME = "sys_config";

    @Override
    public PageData<SysConfigEntity> list(String configKey, int page, int pageSize) {
        LambdaQueryWrapper<SysConfigEntity> wrapper = Wrappers.lambdaQuery(SysConfigEntity.class)
                .like(StringUtils.isNotBlank(configKey), SysConfigEntity::getConfigKey, configKey);
        IPage<SysConfigEntity> iPage = new Page<>(page, pageSize);
        this.page(iPage, wrapper);
        return new PageData<>(iPage);
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "#key.getKey()")
    public <T> void put(BaseKey<T> key, T val) {
        saveOrUpdate(new SysConfigEntity(key.getKey(), key.serialization(val)));
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "#sysConfigEntity.getConfigKey()")
    public void put(SysConfigEntity sysConfigEntity) {
        saveOrUpdate(sysConfigEntity);
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "#key.getKey()")
    public <T> T get(BaseKey<T> key) {
        log.debug("select {}", key);
        SysConfigEntity entity = getById(key.getKey());
        return entity != null ? key.deserialization(entity.getConfigValue()) : key.getDefaultValue();
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "#key.getKey()")
    public <T> T get(BaseKey<T> key, Supplier<T> valueLoad) {
        T val = get(key);
        if (val == null) {
            synchronized (key) {
                val = get(key);
                if (val == null) {
                    val = valueLoad.get();
                    put(key, val);
                }
            }
        }
        return val;
    }

    @Override
    @CacheEvict(cacheNames = CACHE_NAME, key = "#key.getKey()")
    public void del(BaseKey<?> key) {
        removeById(key.getKey());
    }

    //demo
    /*
    public static void main(String[] args) throws Exception {

        SysConfigServiceImpl service = new SysConfigServiceImpl();
        service.put(ConfigKeyConstant.INT_DEMO, 123);
        Integer a = service.get(ConfigKeyConstant.INT_DEMO);
        service.put(ConfigKeyConstant.BOOLEAN_DEMO, true);
        Boolean b = service.get(ConfigKeyConstant.BOOLEAN_DEMO);
        service.put(ConfigKeyConstant.STR_DEMO, "aaaaa");
        String s = service.get(ConfigKeyConstant.STR_DEMO);
        SysUserDTO u = new SysUserDTO();
        u.setId(9999L);
        service.put(ConfigKeyConstant.OBJ_DEMO, u);
        SysUserDTO userDTO = service.get(ConfigKeyConstant.OBJ_DEMO);
        Boolean d = service.get(ConfigKeyConstant.DEFAULT_VAL_DEMO);
        Boolean d2 = service.get(ConfigKeyConstant.DEFAULT_VAL_DEMO);
        service.put(ConfigKeyConstant.LIST_DEMO, Arrays.asList(1, 2, 3));
        List<Integer> l = service.get(ConfigKeyConstant.LIST_DEMO);
        service.put(ConfigKeyConstant.getDynamicKey("1"), "d1");
        service.put(ConfigKeyConstant.getDynamicKey("2"), "d2");
        String d1 = service.get(ConfigKeyConstant.getDynamicKey("1"));
    }

    public static class ConfigKeyConstant {
        // demo
        public static final IntConfigKey INT_DEMO = new IntConfigKey("i");
        public static final BooleanConfigKey BOOLEAN_DEMO = new BooleanConfigKey("b");
        public static final StringConfigKey STR_DEMO = new StringConfigKey("s");
        public static final BooleanConfigKey DEFAULT_VAL_DEMO = new BooleanConfigKey("d", false);
        public static final ObjectConfigKey<SysUserDTO> OBJ_DEMO = new ObjectConfigKey<>("u", SysUserDTO.class);
        public static final ListConfigKey<Integer> LIST_DEMO = new ListConfigKey<>("l", Integer.class);

        public static StringConfigKey getDynamicKey(String keyPrefix) {
            return new StringConfigKey(keyPrefix + "dynamicKey");
        }
    }*/
}
