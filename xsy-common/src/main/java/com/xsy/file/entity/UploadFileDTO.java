package com.xsy.file.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Q1sj
 * @date 2022.12.19 14:13
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UploadFileDTO {
    public final static List<String> IMAGE_FILE_EXTENSION = Collections.unmodifiableList(Arrays.asList("jpg", "png", "gif", "bmp"));
    public final static List<String> VIDEO_FILE_EXTENSION = Collections.unmodifiableList(Arrays.asList("mp4", "avi"));

    private MultipartFile file;
    /**
     * 数据来源
     */
    private String source;
    /**
     * 文件过期时间
     */
    private long expireMs;
    /**
     * 文件最大阈值 单位:byte
     */
    private long maxSize;
    /**
     * 合法文件后缀名
     */
    private List<String> fileExtension;

}
