package com.xsy;

import com.xsy.base.cache.CacheManagerWrapper;
import com.xsy.base.cache.CacheWrapper;
import com.xsy.sys.entity.IntKey;
import com.xsy.sys.entity.ListKey;
import com.xsy.sys.entity.ObjectKey;
import com.xsy.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author Q1sj
 * @date 2023.2.7 13:41
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringCacheManageTest {
    @Autowired
    private CacheManagerWrapper cacheManagerWrapper;

    @Test
    public void put() {
        CacheWrapper cache = cacheManagerWrapper.getCache(DemoKeyConstant.CACHE_NAME);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setId(1L);
        sysUserEntity.setRealName("demo");
        cache.put(DemoKeyConstant.USER_KEY, sysUserEntity);
        cache.put(DemoKeyConstant.USER_LIST_KEY, Arrays.asList(sysUserEntity, sysUserEntity));
        cache.put(DemoKeyConstant.INT_KEY, 999);

        SysUserEntity user = cache.get(DemoKeyConstant.USER_KEY);
        log.info("{} {}", DemoKeyConstant.USER_KEY, user);
        List<SysUserEntity> list = cache.get(DemoKeyConstant.USER_LIST_KEY);
        log.info("{} {}", DemoKeyConstant.USER_LIST_KEY, list);
        Integer i = cache.get(DemoKeyConstant.INT_KEY);
        log.info("{} {}", DemoKeyConstant.INT_KEY, i);
    }

    @Test
    public void delete() {
        CacheWrapper cache = cacheManagerWrapper.getCache(DemoKeyConstant.CACHE_NAME);
        cache.evict(DemoKeyConstant.USER_KEY);
        cache.evict(DemoKeyConstant.USER_LIST_KEY);
        cache.evict(DemoKeyConstant.INT_KEY);
    }

    static class DemoKeyConstant {
        public static final String CACHE_NAME = "demo_cache";
        public static final ObjectKey<SysUserEntity> USER_KEY = new ObjectKey<>("user_1", SysUserEntity.class);
        public static final ListKey<SysUserEntity> USER_LIST_KEY = new ListKey<>("user_list_1", SysUserEntity.class);
        public static final IntKey INT_KEY = new IntKey("int_1");
    }
}
