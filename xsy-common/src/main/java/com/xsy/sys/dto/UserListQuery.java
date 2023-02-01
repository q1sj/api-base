package com.xsy.sys.dto;

import com.xsy.base.pojo.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Q1sj
 * @date 2022.10.17 13:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListQuery extends PageQuery {
    /**
     * 用户名
     */
    private String username;
}
