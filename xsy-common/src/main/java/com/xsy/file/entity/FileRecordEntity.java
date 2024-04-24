package com.xsy.file.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * @author Q1sj
 * @date 2022.8.15 14:09
 */
@Data
@TableName("file_record")
public class FileRecordEntity {
    @TableId
    private Long id;
    /**
     * 原始文件名
     */
    private String name;
    /**
     * 路径 本地存储返回相对路径,其他存储策略返回url或fileId
     */
    private String path;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;
    /**
     * 文件来源
     */
    private String source;
    /**
     * 上传用户id
     */
    private String uploadUserId;
    /**
     * 上传ip
     */
    private String uploadIp;

    private Date uploadTime;
    /**
     * 摘要
     */
    private String digest;
    /**
     * 过期时间 为空永不过期
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date expireTime;
    private Date createTime;

    @TableLogic(delval = "id")
    private Long isDelete;

    private String remark;
}
