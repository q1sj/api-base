package com.xsy.base.log;

import com.xsy.base.util.FileUtils;
import com.xsy.base.util.IOUtils;
import com.xsy.base.util.Result;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 日志下载
 *
 * @author Q1sj
 * @date 2022.9.9 11:12
 */
@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    @Value("${logging.file.name:}")
    private String logFileName;

    /**
     * 日志列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<LogFile>> list() {
        if (StringUtils.isBlank(logFileName)) {
            return Result.ok();
        }
        File logFile = new File(logFileName);
        File[] files;
        if (logFile.isFile()) {
            files = logFile.getParentFile().listFiles();
        } else {
            files = logFile.listFiles();
        }
        List<LogFile> list = Arrays.stream(files).map(LogFile::new).collect(Collectors.toList());
        return Result.ok(list);
    }

    /**
     * 日志下载
     *
     * @param response
     * @param name     文件名
     */
    @GetMapping("/get")
    public void get(HttpServletResponse response, @RequestParam String name) {
        File logFile = new File(logFileName);
        String logPath = logFile.getParentFile().getPath() + "/" + name;
        try (ServletOutputStream os = response.getOutputStream();
             InputStream is = Files.newInputStream(Paths.get(logPath))) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLength(is.available());
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, StandardCharsets.UTF_8.displayName()));
            IOUtils.copy(is, os);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Data
    @NoArgsConstructor
    public static class LogFile {
        private String fileName;
        private String size;
        private Date date;

        public LogFile(File file) {
            Objects.requireNonNull(file);
            this.fileName = file.getName();
            this.size = FileUtils.byteCountToDisplaySize(file.length());
            this.date = new Date(file.lastModified());
        }
    }
}
