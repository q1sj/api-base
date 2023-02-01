/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.security.oauth2;

import com.xsy.base.util.ConvertUtils;
import com.xsy.security.entity.SysUserTokenEntity;
import com.xsy.security.service.AuthService;
import com.xsy.security.service.SysUserTokenService;
import com.xsy.security.user.UserDetail;
import com.xsy.sys.entity.SysUserEntity;
import com.xsy.sys.enums.UserStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
@Component
public class Oauth2Realm extends AuthorizingRealm {
    @Autowired
    private AuthService authService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Oauth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDetail user = (UserDetail) principals.getPrimaryPrincipal();

        //用户权限列表
        Set<String> permsSet = authService.getUserPermissions(user);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        //根据accessToken，查询用户信息
        if (!TokenGenerator.validToken(accessToken)) {
            throw new IncorrectCredentialsException();
        }
        SysUserTokenEntity tokenEntity = authService.getByToken(accessToken);
        //token失效
        if (tokenEntity == null || tokenEntity.getExpireDate().getTime() < System.currentTimeMillis()) {
            throw new ExpiredCredentialsException();
        }

        //查询用户信息
        SysUserEntity userEntity = authService.getUser(tokenEntity.getUserId());

        //转换成UserDetail对象
        UserDetail userDetail = ConvertUtils.sourceToTarget(userEntity, UserDetail.class);

        //账号锁定
        if (userDetail.getStatus() == UserStatusEnum.DISABLE.value()) {
            throw new LockedAccountException("账号锁定");
        }
        sysUserTokenService.refreshExpireDate(tokenEntity);
        return new SimpleAuthenticationInfo(userDetail, accessToken, getName());
    }

}
