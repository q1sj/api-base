/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.24_3306
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : 192.168.2.24:3306
 Source Schema         : web_base_demo

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 10/08/2023 14:58:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_record
-- ----------------------------
DROP TABLE IF EXISTS `file_record`;
CREATE TABLE `file_record`
(
    `id`             bigint(20)                                                     NOT NULL AUTO_INCREMENT,
    `name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `path`           varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `file_type`      varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `file_size`      bigint(20)                                                     NOT NULL,
    `source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `upload_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `upload_ip`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `upload_time`    datetime(6)                                                    NULL     DEFAULT NULL,
    `digest`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `expire_time`    datetime(6)                                                    NULL     DEFAULT NULL,
    `create_time`    datetime(6)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `is_delete`      bigint(20)                                                     NOT NULL DEFAULT 0,
    `remark`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_path` (`path`, `is_delete`) USING BTREE,
    KEY `idx_expire_time` (`expire_time`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_record
-- ----------------------------

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence`
(
    `next_val` bigint(20) NULL DEFAULT NULL
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence`
VALUES (1);
INSERT INTO `hibernate_sequence`
VALUES (1);
INSERT INTO `hibernate_sequence`
VALUES (1);
INSERT INTO `hibernate_sequence`
VALUES (1);
INSERT INTO `hibernate_sequence`
VALUES (1);
INSERT INTO `hibernate_sequence`
VALUES (1);
INSERT INTO `hibernate_sequence`
VALUES (1);

-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`
(
    `job_id`          bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '任务id',
    `bean_name`       varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'spring bean名称',
    `params`          varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
    `cron_expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'cron表达式',
    `status`          tinyint(4)                                                     NULL DEFAULT NULL COMMENT '任务状态  0：正常  1：暂停',
    `remark`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT '备注',
    `create_time`     datetime(0)                                                    NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`job_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1689464453386477571
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '定时任务'
  ROW_FORMAT = Dynamic;


-- ----------------------------
-- Records of schedule_job
-- ----------------------------

-- ----------------------------
-- Table structure for schedule_job_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job_log`;
CREATE TABLE `schedule_job_log`
(
    `log_id`      bigint(20)                                                     NOT NULL AUTO_INCREMENT COMMENT '任务日志id',
    `job_id`      bigint(20)                                                     NOT NULL COMMENT '任务id',
    `bean_name`   varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL COMMENT 'spring bean名称',
    `params`      varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
    `status`      tinyint(4)                                                     NOT NULL COMMENT '任务状态    0：成功    1：失败',
    `error` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失败信息',
    `times`       int(11)                                                        NOT NULL COMMENT '耗时(单位：毫秒)',
    `create_time` datetime(0)                                                    NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`log_id`) USING BTREE,
    INDEX `job_id` (`job_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1689509881821798403
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `config_key`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `config_value_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `config_value`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NOT NULL,
    `remark`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`       datetime(6)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`config_key`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary`
(
    `id`          bigint(20)                                                    NOT NULL,
    `creator`     bigint(20)                                                    NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    `updater`     bigint(20)                                                    NULL DEFAULT NULL,
    `update_date` datetime(6)                                                   NULL DEFAULT NULL,
    `dict_type`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `dict_code`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `dict_value`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `dict_sort`   int(11)                                                       NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_sys_dictionary_type_code` (`dict_type`, `dict_code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`          bigint(20)                                                    NOT NULL,
    `creator`     bigint(20)                                                    NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    `updater`     bigint(20)                                                    NULL DEFAULT NULL,
    `update_date` datetime(6)                                                   NULL DEFAULT NULL,
    `pid`         bigint(20)                                                    NULL DEFAULT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `permissions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `type`        int(11)                                                       NULL DEFAULT NULL,
    `icon`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `sort`        int(11)                                                       NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1067246875800000007, 1067246875800000001, '2022-08-30 10:38:59.000000', 1067246875800000001,
        '2023-12-12 17:06:32.544000', 1067246875800000035, '角色管理', 'sys/role', '', 0, NULL, 5);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1067246875800000025, 1067246875800000001, '2022-08-30 10:38:59.000000', 1067246875800000001,
        '2023-12-13 13:28:00.098000', 1067246875800000035, '菜单管理', 'sys/menu', '', 0, NULL, 3);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1067246875800000035, 1067246875800000001, '2022-08-30 10:38:59.000000', 1067246875800000001,
        '2023-12-12 15:56:37.080000', 0, '系统设置', '', '', 0, 'setting', 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1067246875800000055, 1067246875800000001, '2022-08-30 10:38:59.000000', 1067246875800000001,
        '2023-12-13 13:33:13.186000', 1067246875800000035, '用户配置', 'sys/user', '', 0, NULL, 4);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1721445702183800833, 1067246875800000001, '2023-11-06 16:33:32.973000', 1067246875800000001,
        '2023-12-13 13:27:37.823000', 1067246875800000035, '系统日志', 'sys/log', '', 0, NULL, 1);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1721446737753264129, 1067246875800000001, '2023-11-06 16:37:39.872000', 1067246875800000001,
        '2023-12-13 13:27:48.045000', 1067246875800000035, '参数管理', 'sys/params-manage', '', 0, NULL, 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267238666129410, 1067246875800000001, '2023-11-14 11:25:19.692000', 1067246875800000001,
        '2023-11-14 11:25:19.692000', 1067246875800000055, '新增', NULL, 'user:save', 1, NULL, 1);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267363115323393, 1067246875800000001, '2023-11-14 11:25:49.363000', 1067246875800000001,
        '2023-11-14 11:25:49.363000', 1067246875800000055, '修改', NULL, 'user:update', 1, NULL, 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267459928248322, 1067246875800000001, '2023-11-14 11:26:12.445000', 1067246875800000001,
        '2023-11-14 11:26:12.445000', 1067246875800000055, '删除', NULL, 'user:delete', 1, NULL, 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267572859883522, 1067246875800000001, '2023-11-14 11:26:39.370000', 1067246875800000001,
        '2023-11-14 11:26:39.370000', 1067246875800000007, '新增', NULL, 'role:save', 1, NULL, 1);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267651356282881, 1067246875800000001, '2023-11-14 11:26:58.084000', 1067246875800000001,
        '2023-11-14 11:26:58.084000', 1067246875800000007, '修改', NULL, 'role:update', 1, NULL, 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267726312689666, 1067246875800000001, '2023-11-14 11:27:15.956000', 1067246875800000001,
        '2023-11-14 11:27:15.956000', 1067246875800000007, '删除', NULL, 'role:delete', 1, NULL, 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267809909362690, 1067246875800000001, '2023-11-14 11:27:35.887000', 1067246875800000001,
        '2023-11-14 11:27:35.887000', 1067246875800000025, '新增', NULL, 'menu:save', 1, NULL, 1);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267875466334209, 1067246875800000001, '2023-11-14 11:27:51.517000', 1067246875800000001,
        '2023-11-14 11:27:51.517000', 1067246875800000025, '修改', NULL, 'menu:update', 1, NULL, 2);
INSERT INTO `sys_menu`(`id`, `creator`, `create_date`, `updater`, `update_date`, `pid`, `name`, `url`, `permissions`,
                       `type`, `icon`, `sort`)
VALUES (1724267952003993602, 1067246875800000001, '2023-11-14 11:28:09.765000', 1067246875800000001,
        '2023-11-14 11:28:09.765000', 1067246875800000025, '删除', NULL, 'menu:delete', 1, NULL, 3);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          bigint(20)                                                    NOT NULL,
    `creator`     bigint(20)                                                    NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `remark`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `updater`     bigint(20)                                                    NULL DEFAULT NULL,
    `update_date` datetime(6)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`          bigint(20)  NOT NULL,
    `creator`     bigint(20)  NULL DEFAULT NULL,
    `create_date` datetime(6) NULL DEFAULT NULL,
    `role_id`     bigint(20)  NULL DEFAULT NULL,
    `menu_id`     bigint(20)  NULL DEFAULT NULL,
    `updater`     bigint(20)  NULL DEFAULT NULL,
    `update_date` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`
(
    `id`          bigint(20)  NOT NULL,
    `creator`     bigint(20)  NULL DEFAULT NULL,
    `create_date` datetime(6) NULL DEFAULT NULL,
    `role_id`     bigint(20)  NULL DEFAULT NULL,
    `user_id`     bigint(20)  NULL DEFAULT NULL,
    `updater`     bigint(20)  NULL DEFAULT NULL,
    `update_date` datetime(6) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`          bigint(20)                                                    NOT NULL,
    `creator`     bigint(20)                                                    NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    `username`    varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `real_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `head_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `gender`      int(11)                                                       NULL DEFAULT NULL,
    `email`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `mobile`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `super_admin` int(11)                                                       NULL DEFAULT NULL,
    `status`      int(11)                                                       NULL DEFAULT NULL,
    `updater`     bigint(20)                                                    NULL DEFAULT NULL,
    `update_date` datetime(6)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UK_51bvuyvihefoh4kp5syh2jpi4` (`username`) USING BTREE,
    INDEX `idx_create_date` (`create_date`) USING BTREE,
    INDEX `idx_sys_user_create_date` (`create_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES (845, 1067246875800000001, '2023-04-28 13:51:56.797000', 'qsj',
        '$2a$10$zukqeG9qWMyOcS3MDbUL5.3Kx9A4FZwXVxg9Tk6Ttb2Zj2eS5XoOa', 'gail.wisozk', 'www.micheal-stroman.us', 0,
        'armand.johns@gmail.com', '530-351-3397', 0, 1, 1067246875800000001, '2023-04-28 13:51:56.797000');
INSERT INTO `sys_user`
VALUES (1067246875800000001, 1067246875800000001, '2022-08-30 10:38:59.000000', 'admin',
        '$2a$10$rQ7rc.x/cyAFMEUcVcLM7ufJD8NM1K6uKI5gWFVmjPZmO9Mnm7H6C', '管理员', NULL, 0, 'root@renren.io',
        '13612345678', 1, 1, 1067246875800000001, '2022-08-30 10:38:59.000000');

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`
(
    `id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(20)                                                    NOT NULL,
    `token`       varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `expire_date` datetime(6)                                                   NULL DEFAULT NULL,
    `update_date` datetime(6)                                                   NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UK_343sg1q53w9457ijgmbgyd0dl` (`user_id`) USING BTREE,
    UNIQUE INDEX `UK_p8tl7veg7ajslynlgrn9wr7t5` (`token`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1651826877552832514
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;



SET FOREIGN_KEY_CHECKS = 1;
