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

 Date: 24/04/2023 08:42:31
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
    `source`         varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `upload_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL,
    `upload_ip`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `upload_time`    datetime(6)                                                    NULL     DEFAULT NULL,
    `digest`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `expire_time`    datetime(6)                                                    NULL     DEFAULT NULL,
    `create_time`    datetime(6)                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    `is_delete`      bigint(20)                                                     NOT NULL DEFAULT 0,
    `remark`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_path` (`path`, `is_delete`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `config_key`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci         NULL,
    `remark`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `update_time`  datetime(6)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`config_key`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictionary`;
CREATE TABLE `sys_dictionary`
(
    `id`          bigint(20)                                                    NOT NULL,
    `creator`     bigint(20)                                                    NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    `type`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `code`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `value`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `sort`        int(11)                                                       NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_type_code` (`type`, `code`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`          bigint(20)                                                    NOT NULL,
    `creator`     bigint(20)                                                    NULL DEFAULT NULL,
    `create_date` datetime(6)                                                   NULL DEFAULT NULL,
    `pid`         bigint(20)                                                    NULL DEFAULT NULL,
    `name`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `permissions` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `type`        int(11)                                                       NULL DEFAULT NULL,
    `icon`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `sort`        int(11)                                                       NULL DEFAULT NULL,
    `updater`     bigint(20)                                                    NULL DEFAULT NULL,
    `update_date` datetime(6)                                                   NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_task_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_config`;
CREATE TABLE `sys_task_config`
(
    `id`                  bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `task_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `enable`              bit(1)                                                        NOT NULL,
    `cron_expression`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `allow_re_fires_cron` bit(1)                                                        NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_task_name` (`task_name`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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
    INDEX `idx_create_date` (`create_date`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

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
  AUTO_INCREMENT = 1633001055541338114
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
