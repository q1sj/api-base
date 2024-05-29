package com.xsy.sys.dto;


import lombok.Data;

import java.io.Serializable;


/**
 * @author xsy xsy@xsy.com
 * @since 1.0.0 2023-03-07
 */
@Data
public class SysTaskConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String taskName;

    private Boolean enable;

    private String cronExpression;


}
