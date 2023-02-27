/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.enums.RenConstant;
import com.xsy.base.exception.GlobalException;
import com.xsy.base.service.impl.RenBaseServiceImpl;
import com.xsy.base.util.ConvertUtils;
import com.xsy.base.util.TreeUtils;
import com.xsy.security.config.RequiresPermissionsScan;
import com.xsy.security.enums.SecurityConstant;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.dao.SysMenuDao;
import com.xsy.sys.dto.SysMenuDTO;
import com.xsy.sys.entity.SysMenuEntity;
import com.xsy.sys.enums.SuperAdminEnum;
import com.xsy.sys.service.SysMenuService;
import com.xsy.sys.service.SysRoleMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SysMenuServiceImpl extends RenBaseServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private RequiresPermissionsScan requiresPermissionsScan;

    @Override
    public SysMenuDTO get(Long id) {
        SysMenuEntity entity = baseDao.getById(id);

        SysMenuDTO dto = ConvertUtils.sourceToTarget(entity, SysMenuDTO.class);

        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);

        //保存菜单
        insert(entity);
    }

    @Override
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(SysMenuDTO dto) {
        SysMenuEntity entity = ConvertUtils.sourceToTarget(dto, SysMenuEntity.class);
        //上级菜单不能为自身
        if (entity.getId().equals(entity.getPid())) {
            throw new GlobalException("上级菜单不能为自身");
        }

        //更新菜单
        updateById(entity);
    }

    @Override
    @CacheEvict(cacheNames = SecurityConstant.SYS_USER_PERMISSIONS_CACHE_NAME, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //删除菜单
        deleteById(id);

        //删除角色菜单关系
        sysRoleMenuService.deleteByMenuId(id);
    }

    @Override
    public List<SysMenuDTO> getAllMenuList(Integer type) {
        List<SysMenuEntity> menuList = baseDao.getMenuList(type);

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList, RenConstant.MENU_ROOT);
    }

    @Override
    public List<SysMenuDTO> getUserMenuList(UserDetail user, Integer type) {
        List<SysMenuEntity> menuList;

        //系统管理员，拥有最高权限
        if (user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            menuList = baseDao.getMenuList(type);
        } else {
            menuList = baseDao.getUserMenuList(user.getId(), type);
        }

        List<SysMenuDTO> dtoList = ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);

        return TreeUtils.build(dtoList);
    }

    @Override
    public List<String> getUserPermissionsList(Long userId) {
        return baseDao.getUserPermissionsList(userId);
    }

    @Override
    public List<SysMenuDTO> getListPid(Long pid) {
        List<SysMenuEntity> menuList = baseDao.getListPid(pid);

        return ConvertUtils.sourceToTarget(menuList, SysMenuDTO.class);
    }

    @Override
    public List<String> getAllPermissions() {
        return requiresPermissionsScan.getPermissionList();
    }

    @Component
    static class MenuInit implements CommandLineRunner {
        Logger log = LoggerFactory.getLogger(getClass());

        String[] sqls = {
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000003, 1067246875800000055, '新增', NULL, 'sys:user:save,sys:dept:list,sys:role:list', 1, NULL, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000004, 1067246875800000055, '修改', NULL, 'sys:user:update,sys:dept:list,sys:role:list', 1, NULL, 2, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000005, 1067246875800000055, '删除', NULL, 'sys:user:delete', 1, NULL, 3, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000006, 1067246875800000055, '导出', NULL, 'sys:user:export', 1, NULL, 4, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000007, 1067246875800000035, '角色管理', 'sys/role', '', 0, 'icon-team', 2, 1067246875800000001, '2022-08-30 10:38:59', NULL, NULL);",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000008, 1067246875800000007, '查看', NULL, 'sys:role:page,sys:role:info', 1, NULL, 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000009, 1067246875800000007, '新增', NULL, 'sys:role:save,sys:menu:select,sys:dept:list', 1, NULL, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000010, 1067246875800000007, '修改', NULL, 'sys:role:update,sys:menu:select,sys:dept:list', 1, NULL, 2, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000011, 1067246875800000007, '删除', NULL, 'sys:role:delete', 1, NULL, 3, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000025, 1067246875800000035, '菜单管理', 'sys/menu', '0', 0, 'icon-unorderedlist', 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-09-15 16:50:38');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000026, 1067246875800000025, '查看', NULL, 'sys:menu:list,sys:menu:info', 1, NULL, 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000027, 1067246875800000025, '新增', NULL, 'sys:menu:save', 1, NULL, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000028, 1067246875800000025, '修改', NULL, 'sys:menu:update', 1, NULL, 2, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000029, 1067246875800000025, '删除', NULL, 'sys:menu:delete', 1, NULL, 3, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000035, 0, '系统设置', '', '', 0, 'icon-setting', 3, 1067246875800000001, '2022-08-30 10:38:59', NULL, NULL);",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000055, 1067246875800000035, '用户管理', 'sys/user', '', 0, 'icon-user', 0, 1067246875800000001, '2022-08-30 10:38:59', NULL, NULL);",
                "INSERT INTO `sys_menu`(id,pid,`name`,url,permissions,`type`,icon,sort,creator,create_date,updater,update_date) VALUES (1067246875800000056, 1067246875800000055, '查看', NULL, 'sys:user:page,sys:user:info', 1, NULL, 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');"
        };
        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Autowired
        private SysMenuDao sysMenuDao;

        @Override
        public void run(String... args) throws Exception {
            new Thread(() -> {
                try {
                    // 等待jpa初始化表结构
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Long count = sysMenuDao.selectCount(Wrappers.emptyWrapper());
                if (count > 0) {
                    return;
                }
                log.info("init menu");
                for (String sql : sqls) {
                    log.info("{}", sql);
                    jdbcTemplate.execute(sql);
                }
            }, "menu-init").start();
        }
    }
}
