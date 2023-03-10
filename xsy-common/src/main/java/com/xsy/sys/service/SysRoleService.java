/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.sys.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xsy.base.service.RenBaseService;
import com.xsy.base.util.PageData;
import com.xsy.sys.dto.RoleListQuery;
import com.xsy.sys.dto.SysRoleDTO;
import com.xsy.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysRoleService extends RenBaseService<SysRoleEntity> {

    PageData<SysRoleDTO> page(RoleListQuery query);

    List<SysRoleDTO> list(RoleListQuery query);

    SysRoleDTO get(Long id);

    void save(SysRoleDTO dto);

    void update(SysRoleDTO dto);

    void delete(Long[] ids);

}
