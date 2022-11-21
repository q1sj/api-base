package com.xsy.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    private Integer fileSize;
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
    /**
     * 过期时间 为空永不过期
     */
    private Date expireTime;

    private String md5;

    private Date createTime;
    private Date uploadTime;
    private String remark;
}
