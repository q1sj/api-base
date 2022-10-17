package com.xsy.sys.dto;

import com.xsy.base.pojo.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author Q1sj
 * @date 2022.10.17 13:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleListQuery extends PageQuery {
    /**
     * 角色名称
     */
    private String name;
}
