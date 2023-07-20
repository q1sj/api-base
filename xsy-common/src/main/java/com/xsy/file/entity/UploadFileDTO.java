package com.xsy.file.entity;

import com.xsy.base.util.FileUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2022.12.19 14:13
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UploadFileDTO {
    public final static Set<String> IMAGE_FILE_EXTENSION = new HashSet<>(Arrays.asList("jpg", "png", "gif", "bmp"));
    public final static Set<String> VIDEO_FILE_EXTENSION = new HashSet<>(Arrays.asList("mp4", "avi"));
    /**
     * expireMs < 0 永不过期
     */
    public final static int NO_EXPIRE = -1;
    private MultipartFile file;
    /**
     * 数据来源
     */
    private String source;
    /**
     * 文件过期时间
     */
    private long expireMs = TimeUnit.DAYS.toMillis(1);
    /**
     * 文件最大阈值 单位:byte
     */
    private long maxSize = FileUtils.ONE_GB;
    /**
     * 合法文件后缀名
     */
    @Nullable
    private Set<String> fileExtension;

    public UploadFileDTO setFileExtension(@Nullable Set<String> fileExtension) {
        this.fileExtension = fileExtension;
        return this;
    }

    public UploadFileDTO setFileExtension(String... fileExtension) {
        this.fileExtension = new HashSet<>(Arrays.asList(fileExtension));
        return this;
    }

}
