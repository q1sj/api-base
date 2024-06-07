/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.xsy.security.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.lang.Nullable;

/**
 * 用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
public class SecurityUser {

    /**
     * 获取用户信息
     */
    public static UserDetail getUser() {
        Subject subject = getSubject();
        if (subject == null) {
            return new UserDetail();
        }
        UserDetail user = null;
        try {
            user = (UserDetail) subject.getPrincipal();
        } catch (Exception e) {
            log.warn("用户获取失败 {}", e.getMessage());
            return new UserDetail();
        }
        if (user == null) {
            return new UserDetail();
        }
        return user;
    }

    @Nullable
    private static Subject getSubject() {
        try {
            return SecurityUtils.getSubject();
        } catch (Exception e) {
            log.error("用户获取失败 {}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取用户ID
     */
    @Nullable
    public static Long getUserId() {
        return getUser().getId();
    }
}
