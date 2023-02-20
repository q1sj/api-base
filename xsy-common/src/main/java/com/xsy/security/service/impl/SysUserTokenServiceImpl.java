/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.service.impl;

import com.xsy.base.cache.CacheManagerWrapper;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.security.dao.SysUserTokenDao;
import com.xsy.security.dto.TokenDTO;
import com.xsy.security.entity.SysUserTokenEntity;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.oauth2.TokenGenerator;
import com.xsy.security.service.SysUserTokenService;
import com.xsy.sys.entity.LongKey;
import com.xsy.sys.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@CacheConfig(cacheNames = SecurityConstant.SYS_USER_TOKEN_CACHE_NAME)
public class SysUserTokenServiceImpl extends RenBaseServiceImpl<SysUserTokenDao, SysUserTokenEntity> implements SysUserTokenService {
    @Autowired
    private SysConfigService sysConfigService;
    private static final LongKey EXPIRE_HOURS_KEY = new LongKey("EXPIRE_HOURS_KEY", 12L);
    /**
     * token过期时间小于此值 刷新
     */
    private static final LongKey REFRESH_HOURS_KEY = new LongKey("REFRESH_HOURS_KEY", 6L);

    private final Cache cache;

    public SysUserTokenServiceImpl(CacheManagerWrapper cacheManagerWrapper) {
        cache = cacheManagerWrapper.getCache(SecurityConstant.SYS_USER_TOKEN_CACHE_NAME);
    }

    @Override
    public TokenDTO createToken(Long userId) {
        logout(userId);
        //用户token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + getExpireMs());

        //判断是否生成过token
        SysUserTokenEntity tokenEntity = baseDao.getByUserId(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateDate(now);
            tokenEntity.setExpireDate(expireTime);

            //保存token
            this.insert(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateDate(now);
            tokenEntity.setExpireDate(expireTime);

            //更新token
            this.updateById(tokenEntity);
        }
        return new TokenDTO(token, getExpireMs());
    }


    @Override
    @Cacheable(key = "T(com.xsy.security.enums.SecurityConstant).getSysUserTokenCacheKey(#token)")
    public SysUserTokenEntity getByToken(String token) {
        return baseDao.getByToken(token);
    }

    /**
     * 登出
     * <p>
     * 1.根据userId查询数据库token
     * 2.根据token删除缓存
     * 3.修改数据库
     *
     * @param userId 用户ID
     */
    @Override
    public void logout(Long userId) {
        SysUserTokenEntity user = baseDao.getByUserId(userId);
        if (user == null) {
            return;
        }
        // 存在内部调用 不使用注解
        cache.evict(SecurityConstant.getSysUserTokenCacheKey(user.getToken()));
        //生成一个token
        String token = TokenGenerator.generateValue();

        //修改token
        baseDao.updateToken(userId, token);
    }

    @Override
    public void refreshExpireDate(SysUserTokenEntity tokenEntity) {
        String accessToken = tokenEntity.getToken();
        long expireTime = tokenEntity.getExpireDate().getTime();
        // 剩余时间大于RESIDUE_MS 不刷新过期时间
        if (expireTime - System.currentTimeMillis() > getRefreshMs()) {
            return;
        }
        log.debug("刷新token:{}过期时间", accessToken);
        tokenEntity.setExpireDate(new Date(System.currentTimeMillis() + getExpireMs()));
        cache.evict(SecurityConstant.getSysUserTokenCacheKey(accessToken));
        this.updateById(tokenEntity);
    }

    private long getExpireMs() {
        return TimeUnit.HOURS.toMillis(sysConfigService.get(EXPIRE_HOURS_KEY));
    }

    private long getRefreshMs() {
        return TimeUnit.HOURS.toMillis(sysConfigService.get(REFRESH_HOURS_KEY));
    }
}
