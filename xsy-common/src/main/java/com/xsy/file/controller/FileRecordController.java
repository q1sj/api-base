package com.xsy.file.controller;

import com.xsy.base.util.FileUtils;
import com.xsy.base.util.Result;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 文件管理
 *
 * @author Q1sj
 * @date 2022.8.30 15:53
 */
@Slf4j
@RequestMapping(FileRecordController.REQUEST_MAPPING)
@RestController
public class FileRecordController {
    public static final String REQUEST_MAPPING = "/file";
    @Autowired
    private FileRecordService fileRecordService;

    /**
     * 上传文件 demo
     * 根据具体业务单独编写接口 设置文件大小阈值,合法后缀名
     *
     * @param file
     * @return
     */
//    @PostMapping("/upload")
    public Result<FileRecordEntity> upload(MultipartFile file) {
        String source = "upload-api-demo";
        try {
            FileRecordEntity record = fileRecordService.upload(file, source,
                    TimeUnit.DAYS.toMillis(1),
                    10 * FileUtils.ONE_MB,
                    Arrays.asList("jpg", "png"));
            return Result.ok(record);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.error("上传失败");
        }
    }

    public static final String DOWNLOAD_MAPPING = "/download";

    /**
     * 下载文件
     *
     * @param response
     * @param path     {@link FileRecordEntity#getPath()}
     */
    @GetMapping(DOWNLOAD_MAPPING)
    public void download(HttpServletResponse response, @RequestParam String path) {
        try (OutputStream os = response.getOutputStream()) {
            byte[] fileBytes = this.fileRecordService.getFileBytes(path);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(path, StandardCharsets.UTF_8.displayName()));
            os.write(fileBytes);
        } catch (IOException e) {
            log.warn("文件获取失败", e);
        }
    }
}

