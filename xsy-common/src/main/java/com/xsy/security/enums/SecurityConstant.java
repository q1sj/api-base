package com.xsy.security.enums;

/**
 * @author Q1sj
 * @date 2022.9.29 11:24
 */
public class SecurityConstant {
    /**
     * token header
     */
    public static final String TOKEN_HEADER = "token";
    /**
     * 权限分隔符
     */
    public static final String PERMISSIONS_SEPARATOR = ",";

    /**
     * token缓存名称
     */
    public static final String SYS_USER_TOKEN_CACHE_NAME = "user_token";
    /**
     * token缓存key前缀
     */
    public static final String SYS_USER_TOKEN_CACHE_KEY_PREFIX = "user_token_";

    public static final String SYS_USER_CACHE_NAME = "sys_user";

    public static final String SYS_USER_CACHE_KEY_PREFIX = "sys_user_";

    /**
     * 用户权限缓存名称
     */
    public static final String SYS_USER_PERMISSIONS_CACHE_NAME = "user_permissions";
    /**
     * 用户权限缓存key前缀
     */
    public static final String SYS_USER_PERMISSIONS_CACHE_KEY_PREFIX = "user_permissions_";


    /**
     * 获取缓存key
     *
     * @param token
     * @return
     */
    public static String getSysUserTokenCacheKey(String token) {
        return SYS_USER_TOKEN_CACHE_KEY_PREFIX + token;
    }

    public static String getSysUserCacheKey(Long userId) {
        return SYS_USER_CACHE_KEY_PREFIX + userId;
    }

    /**
     * 获取用户权限key
     *
     * @param userId
     * @return
     */
    public static String getSysUserPermissionsCacheKey(Long userId) {
        return SYS_USER_PERMISSIONS_CACHE_KEY_PREFIX + userId;
    }
}
