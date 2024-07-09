/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.exception.UserLockedException;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.base.util.BizAssertUtils;
import com.xsy.base.util.ConvertUtils;
import com.xsy.base.util.PageData;
import com.xsy.security.dto.LoginDTO;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.password.PasswordUtils;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dao.SysUserDao;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.dto.UserListQuery;
import com.xsy.sys.entity.SysUserEntity;
import com.xsy.sys.enums.SuperAdminEnum;
import com.xsy.sys.enums.UserStatusEnum;
import com.xsy.sys.service.SysRoleMenuService;
import com.xsy.sys.service.SysRoleUserService;
import com.xsy.sys.service.SysUserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
@CacheConfig(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME)
public class SysUserServiceImpl extends RenBaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
    private static final int MAX_WRONG_PASSWORD_COUNT = 5;
    private static final int WRONG_PASSWORD_RECORD_EXPIRED = 30;
    private final Map<String, WrongPasswordRecord> wrongPasswordRecordMap = new ConcurrentHashMap<>();
    @Autowired
    private SysRoleUserService sysRoleUserService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public PageData<SysUserDTO> page(UserListQuery query) {
        LambdaQueryWrapper<SysUserEntity> wrapper = getWrapper(query);
        IPage<SysUserEntity> iPage = baseDao.selectPage(query.initPage(), wrapper);
        return getPageData(iPage, SysUserDTO.class);
    }

    @Override
    protected <T> PageData<T> getPageData(List<?> list, long total, Class<T> target) {
        List<T> targetList = ConvertUtils.sourceToTarget(list, target);

        return new PageData<>(targetList, total);
    }

    @Override
    protected <T> PageData<T> getPageData(IPage page, Class<T> target) {
        return getPageData(page.getRecords(), page.getTotal(), target);
    }

    @Override
    public SysUserDTO get(Long id) {

        SysUserEntity entity = this.selectById(id);

        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    @Cacheable(key = "T(com.xsy.security.enums.SecurityConstant).getSysUserCacheKey(#id)")
    public SysUserEntity selectById(Serializable id) {
        return baseDao.selectById(id);
    }

    @Override
    public SysUserDTO getByUsername(String username) {
        SysUserEntity entity = baseDao.getByUsername(username);
        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);

        //密码加密
        String password = PasswordUtils.encode(entity.getPassword());
        entity.setPassword(password);
        entity.setStatus(UserStatusEnum.ENABLED.value());
        //保存用户
        entity.setSuperAdmin(SuperAdminEnum.NO.value());
        insert(entity);

        //保存角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME, key = "T(com.xsy.security.enums.SecurityConstant).getSysUserCacheKey(#dto.getId())"),
            @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, key = "T(com.xsy.security.enums.SecurityConstant).getSysUserPermissionsCacheKey(#dto.getId())")
    })
    public void update(SysUserDTO dto) {
        SysUserEntity entity = ConvertUtils.sourceToTarget(dto, SysUserEntity.class);
        //密码加密
        if (StringUtils.isBlank(dto.getPassword())) {
            entity.setPassword(null);
        } else {
            String password = PasswordUtils.encode(entity.getPassword());
            entity.setPassword(password);
        }

        //更新用户
        updateById(entity);

        //更新角色用户关系
        sysRoleUserService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
    }

    @Override
    public void updateLastLoginTime(Long id) {
        SysUserEntity user = new SysUserEntity();
        user.setId(id);
        user.setLastLoginTime(new Date());
        updateById(user);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME, key = "'sys_user_'+#ids"),
            @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, key = "'user_permissions_'+#ids"),
    })
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        //删除用户
        baseDao.deleteBatchIds(Arrays.asList(ids));

        //删除角色用户关系
        sysRoleUserService.deleteByUserIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_CACHE_NAME, key = "'sys_user_'+#id")
    public void updatePassword(Long id, String newPassword) {
        BizAssertUtils.isTrue(SecurityUser.getUser().isAdmin(), "只允许超级管理员重置密码");
        newPassword = PasswordUtils.encode(newPassword);
        baseDao.updatePassword(id, newPassword);
    }

    @Override
    public SysUserDTO validLogin(LoginDTO login) throws GlobalException {
        // 密码连续错误锁定账号
        String username = login.getUsername();
        if (passwordErrorExceeded(username)) {
            throw new UserLockedException("账户锁定");
        }
        // 根据用户名获取用户
        //用户信息
        SysUserEntity user = baseDao.getByUsername(username);
        if (user == null) {
            int wrongPasswordCount = accumulateWrongPasswords(username);
            throw new GlobalException("用户名或密码错误" + wrongPasswordCount + "次,超过" + MAX_WRONG_PASSWORD_COUNT + "锁定账户");
        }
        //账号停用
        if (userIsDisable(user)) {
            throw new UserLockedException("账户锁定");
        }
        // 密码错误
        if (!PasswordUtils.matches(login.getPassword(), user.getPassword())) {
            // 累加错误次数
            int wrongPasswordCount = accumulateWrongPasswords(username);
            throw new GlobalException("用户名或密码错误" + wrongPasswordCount + "次,超过" + MAX_WRONG_PASSWORD_COUNT + "锁定账户");
        }
        clearWrongPasswordCount(username);
        SysUserDTO dto = new SysUserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public boolean userIsDisable(SysUserEntity user) {
        return Objects.equals(user.getStatus(), UserStatusEnum.DISABLE.value());
    }

    @Override
    public List<Long> allMenuId(long id) {
        List<Long> roleIdList = sysRoleUserService.getRoleIdList(id);
        return sysRoleMenuService.getMenuIdList(roleIdList);
    }

    private LambdaQueryWrapper<SysUserEntity> getWrapper(UserListQuery query) {
        UserDetail user = SecurityUser.getUser();
        return Wrappers.lambdaQuery(SysUserEntity.class)
                .eq(!user.isAdmin(), SysUserEntity::getCreator, user.getId())
                .like(StringUtils.isNotBlank(query.getUsername()), SysUserEntity::getUsername, query.getUsername());
    }

    /**
     * 累加密码错误次数
     *
     * @param username
     */
    private int accumulateWrongPasswords(String username) {
        return getWrongPasswordRecord(username).getWrongCount().incrementAndGet();
    }

    /**
     * 用户是否超过密码错误次数
     * x分钟内错误次数大于x次
     *
     * @param username
     * @return
     */
    private boolean passwordErrorExceeded(String username) {
        WrongPasswordRecord record = getWrongPasswordRecord(username);
        // x分钟内错误次数大于x次 锁定
        if (record.getWrongCount().intValue() >= MAX_WRONG_PASSWORD_COUNT) {
            if (record.getLastWrongTime() + TimeUnit.MINUTES.toMillis(WRONG_PASSWORD_RECORD_EXPIRED) > System.currentTimeMillis()) {
                return true;
            } else {
                // 超出时间 清空错误次数
                clearWrongPasswordCount(username);
                return false;
            }
        }
        return false;
    }

    private void clearWrongPasswordCount(String username) {
        getWrongPasswordRecord(username).getWrongCount().set(0);
    }

    private WrongPasswordRecord getWrongPasswordRecord(String username) {
        return wrongPasswordRecordMap.computeIfAbsent(username, k -> new WrongPasswordRecord(username, new AtomicInteger(0), 0L));
    }

    /**
     * 用户密码错误次数记录
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WrongPasswordRecord {
        /**
         * 用户名
         */
        private String username;
        /**
         * 错误次数
         */
        private AtomicInteger wrongCount;
        /**
         * 上次密码错误时间
         */
        private Long lastWrongTime;
    }
}
