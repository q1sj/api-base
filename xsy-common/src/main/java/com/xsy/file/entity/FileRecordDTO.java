package com.xsy.file.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2022.12.20 15:20
 */
@Data
@NoArgsConstructor
public class FileRecordDTO {
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
    /**
     * 过期时间 为空永不过期
     */
    private Date expireTime;
    /**
     * 摘要
     */
    private String digest;

    private Date createTime;
    private Date uploadTime;
    private String remark;

    private InputStream content;
}
