/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.2.24_3306
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : 192.168.2.24:3306
 Source Schema         : bay_parking_init

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 21/11/2022 15:04:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for file_record
-- ----------------------------
DROP TABLE IF EXISTS `file_record`;
CREATE TABLE `file_record`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名称',
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `file_type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件类型',
  `file_size` int(16) NOT NULL COMMENT '文件大小byte',
  `source` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传来源',
  `upload_user_id` varchar(62) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传用户id',
  `upload_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '上传用户ip',
  `md5` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间 为空永不过期',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `upload_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uni_path`(`path`) USING BTREE,
  INDEX `idx_expire_time`(`expire_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`config_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `pid` bigint(20) NULL DEFAULT NULL COMMENT '上级ID，一级菜单为0',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `permissions` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:list,sys:user:save)',
  `type` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '类型   0：菜单   1：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_pid`(`pid`) USING BTREE,
  INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1067246875800000003, 1067246875800000055, '新增', NULL, 'sys:user:save,sys:dept:list,sys:role:list', 1, NULL, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000004, 1067246875800000055, '修改', NULL, 'sys:user:update,sys:dept:list,sys:role:list', 1, NULL, 2, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000005, 1067246875800000055, '删除', NULL, 'sys:user:delete', 1, NULL, 3, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000006, 1067246875800000055, '导出', NULL, 'sys:user:export', 1, NULL, 4, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000007, 1067246875800000035, '角色管理', 'sys/role', '', 0, 'icon-team', 2, 1067246875800000001, '2022-08-30 10:38:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1067246875800000008, 1067246875800000007, '查看', NULL, 'sys:role:page,sys:role:info', 1, NULL, 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000009, 1067246875800000007, '新增', NULL, 'sys:role:save,sys:menu:select,sys:dept:list', 1, NULL, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000010, 1067246875800000007, '修改', NULL, 'sys:role:update,sys:menu:select,sys:dept:list', 1, NULL, 2, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000011, 1067246875800000007, '删除', NULL, 'sys:role:delete', 1, NULL, 3, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000025, 1067246875800000035, '菜单管理', 'sys/menu', '0', 0, 'icon-unorderedlist', 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-09-15 16:50:38');
INSERT INTO `sys_menu` VALUES (1067246875800000026, 1067246875800000025, '查看', NULL, 'sys:menu:list,sys:menu:info', 1, NULL, 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000027, 1067246875800000025, '新增', NULL, 'sys:menu:save', 1, NULL, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000028, 1067246875800000025, '修改', NULL, 'sys:menu:update', 1, NULL, 2, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000029, 1067246875800000025, '删除', NULL, 'sys:menu:delete', 1, NULL, 3, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');
INSERT INTO `sys_menu` VALUES (1067246875800000035, 0, '系统设置', '', '', 0, 'icon-setting', 3, 1067246875800000001, '2022-08-30 10:38:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1067246875800000055, 1067246875800000035, '用户管理', 'sys/user', '', 0, 'icon-user', 0, 1067246875800000001, '2022-08-30 10:38:59', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1067246875800000056, 1067246875800000055, '查看', NULL, 'sys:user:page,sys:user:info', 1, NULL, 0, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dept_id`(`dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色用户关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `head_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `gender` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '性别   0：男   1：女    2：保密',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门ID',
  `super_admin` tinyint(3) UNSIGNED NULL DEFAULT NULL COMMENT '超级管理员   0：否   1：是',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：停用   1：正常',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新者',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE,
  INDEX `idx_create_date`(`create_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1067246875800000001, 'admin', '$2a$10$012Kx2ba5jzqr9gLlG4MX.bnQJTD9UWqF57XDo2N3.fPtLne02u/m', '管理员', NULL, 0, 'root@renren.io', '13612345678', NULL, 1, 1, 1067246875800000001, '2022-08-30 10:38:59', 1067246875800000001, '2022-08-30 10:38:59');

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户token',
  `expire_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
  UNIQUE INDEX `token`(`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
