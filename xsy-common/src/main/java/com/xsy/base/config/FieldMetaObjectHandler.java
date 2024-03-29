package com.xsy.base.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xsy.base.pojo.BaseEntity;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * 公共字段，自动填充值
 *
 * @author Mark sunlightcs@gmail.com
 */
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = BaseEntity.Fields.createDate;
    private final static String CREATOR = BaseEntity.Fields.creator;
    private final static String UPDATE_DATE = BaseEntity.Fields.updateDate;
    private final static String UPDATER = BaseEntity.Fields.updater;

    @Override
    public void insertFill(MetaObject metaObject) {
        UserDetail user = SecurityUser.getUser();
        Date date = new Date();

        //创建者
        strictInsertFill(metaObject, CREATOR, Long.class, user.getId());
        //创建时间
        strictInsertFill(metaObject, CREATE_DATE, Date.class, date);
        //更新者
        strictInsertFill(metaObject, UPDATER, Long.class, user.getId());
        //更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新者
        strictUpdateFill(metaObject, UPDATER, Long.class, SecurityUser.getUserId());
        //更新时间
        strictUpdateFill(metaObject, UPDATE_DATE, Date.class, new Date());
    }
}
