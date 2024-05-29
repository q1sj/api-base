ALTER TABLE `sys_user`
    ADD COLUMN `last_login_time` datetime NULL COMMENT '上次登录时间' AFTER `status`;