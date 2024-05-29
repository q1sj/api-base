package com.xsy.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Q1sj
 * @date 2022.10.9 16:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {

    private String token;
    /**
     * 有效期 毫秒
     */
    private Long expire;
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
}
