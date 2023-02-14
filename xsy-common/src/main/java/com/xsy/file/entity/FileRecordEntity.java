package com.xsy.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2022.8.15 14:09
 */
@Data
@TableName("file_record")
@Entity(name = "file_record")
@Table(indexes = {@Index(name = "uk_path", columnList = "path", unique = true)})
public class FileRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId
    private Long id;
    /**
     * 原始文件名
     */
    @Column(nullable = false)
    private String name;
    /**
     * 路径 本地存储返回相对路径,其他存储策略返回url或fileId
     */
    @Column(nullable = false, length = 512)
    private String path;
    /**
     * 文件类型
     */
    @Column(nullable = false, length = 16)
    private String fileType;
    /**
     * 文件大小
     */
    @Column(nullable = false)
    private Long fileSize;
    /**
     * 文件来源
     */
    @Column(nullable = false, length = 16)
    private String source;
    /**
     * 上传用户id
     */
    @Column(nullable = false, length = 64)
    private String uploadUserId;
    /**
     * 上传ip
     */
    @Column(nullable = false)
    private String uploadIp;

    private Date uploadTime;
    /**
     * 摘要
     */
    @Column(nullable = false)
    private String digest;
    /**
     * 过期时间 为空永不过期
     */
    private Date expireTime;
    @Column(nullable = false)
    @ColumnDefault("CURRENT_TIMESTAMP(6)")
    private Date createTime;

    @Column(nullable = false)
    @ColumnDefault("0")
    @TableLogic
    private Boolean isDelete;
    @Column(length = 2048)
    private String remark;
}
