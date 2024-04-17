CREATE TABLE `export_record`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '导出记录ID',
    `file_name`   varchar(32)  NOT NULL COMMENT '文件名称',
    `code`        varchar(100) NOT NULL COMMENT '导出类型id',
    `code_name`   varchar(255) NOT NULL COMMENT '导出类型名称 例如：交通事件',
    `conditions`  text COMMENT '下载条件',
    `export_time` int(11)    DEFAULT NULL COMMENT '导出时长（秒）',
    `status`      int(2)       NOT NULL COMMENT '导出状态：1等待导出、2导出中、3导出成功、4导出失败',
    `status_name` varchar(20)  NOT NULL,
    `fail_reason` text COMMENT '导出失败原因',
    `file_id`     bigint(20) DEFAULT NULL COMMENT '文件id',
    `user_id`     bigint(20)   NOT NULL COMMENT '用户ID',
    `user_name`   varchar(50)  NOT NULL COMMENT '用户名',
    `create_time` datetime     NOT NULL COMMENT '创建时间',
    `update_time` datetime   DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `progress`    int(11)      NOT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1770679294167740419
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC COMMENT ='导出记录表';
