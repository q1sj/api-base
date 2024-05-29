package com.xsy.sys.dto;

import com.xsy.base.pojo.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询参数
 *
 * @author xsy xsy@xsy.com
 * @since 1.0.0 2023-03-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTaskConfigQuery extends PageQuery {
    private String taskName;
}
