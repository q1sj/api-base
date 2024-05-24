package com.xsy.base.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xsy.base.pojo.BaseEntity;
import com.xsy.security.user.SecurityUser;
import com.xsy.security.user.UserDetail;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 公共字段，自动填充值
 *
 * @author Mark sunlightcs@gmail.com
 */
public class FieldMetaObjectHandler implements MetaObjectHandler {
    private final static String CREATE_DATE = BaseEntity.Fields.createDate;
    private final static String CREATE_TIME = "createTime";
    private final static String CREATOR = BaseEntity.Fields.creator;
    private final static String UPDATE_DATE = BaseEntity.Fields.updateDate;
    private final static String UPDATE_TIME = "updateTime";
    private final static String UPDATER = BaseEntity.Fields.updater;

    @Override
    public void insertFill(MetaObject metaObject) {
        UserDetail user = SecurityUser.getUser();
        Date date = new Date();

        //创建者
        strictInsertFill(metaObject, CREATOR, Long.class, user.getId());
        //创建时间
        strictInsertFill(metaObject, CREATE_DATE, Date.class, date);
        strictInsertFill(metaObject, CREATE_DATE, LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, CREATE_TIME, Date.class, date);
        strictInsertFill(metaObject, CREATE_TIME, LocalDateTime.class, LocalDateTime.now());
        //更新者
        strictInsertFill(metaObject, UPDATER, Long.class, user.getId());
        //更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);
        strictInsertFill(metaObject, UPDATE_DATE, LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, UPDATE_TIME, Date.class, date);
        strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新者
        strictUpdateFill(metaObject, UPDATER, Long.class, SecurityUser.getUserId());
        //更新时间
        Date date = new Date();
        strictInsertFill(metaObject, UPDATE_DATE, Date.class, date);
        strictInsertFill(metaObject, UPDATE_DATE, LocalDateTime.class, LocalDateTime.now());
        strictInsertFill(metaObject, UPDATE_TIME, Date.class, date);
        strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime.class, LocalDateTime.now());
    }

    /**
     * 如果填充的值不为null 覆盖原始值
     *
     * @param metaObject metaObject meta object parameter
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value of Supplier
     * @return
     */
    @Override
    public MetaObjectHandler strictFillStrategy(MetaObject metaObject, String fieldName, Supplier<?> fieldVal) {
        Object obj = fieldVal.get();
        if (Objects.nonNull(obj)) {
            metaObject.setValue(fieldName, obj);
        }
        return this;
    }
}
