package com.xsy.base.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xsy.base.pojo.BaseEntity;
import com.xsy.security.user.SecurityUser;
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
        //创建者
        strictInsertFill(metaObject, CREATOR, SecurityUser::getUserId, Long.class);
        //创建时间
        strictInsertFill(metaObject, CREATE_DATE, Date::new, Date.class);
        strictInsertFill(metaObject, CREATE_DATE, LocalDateTime::now, LocalDateTime.class);
        strictInsertFill(metaObject, CREATE_TIME, Date::new, Date.class);
        strictInsertFill(metaObject, CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        //更新者
        strictInsertFill(metaObject, UPDATER, SecurityUser::getUserId, Long.class);
        //更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Date::new, Date.class);
        strictInsertFill(metaObject, UPDATE_DATE, LocalDateTime::now, LocalDateTime.class);
        strictInsertFill(metaObject, UPDATE_TIME, Date::new, Date.class);
        strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //更新者
        strictUpdateFill(metaObject, UPDATER, SecurityUser::getUserId, Long.class);
        //更新时间
        strictInsertFill(metaObject, UPDATE_DATE, Date::new, Date.class);
        strictInsertFill(metaObject, UPDATE_DATE, LocalDateTime::now, LocalDateTime.class);
        strictInsertFill(metaObject, UPDATE_TIME, Date::new, Date.class);
        strictInsertFill(metaObject, UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
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
