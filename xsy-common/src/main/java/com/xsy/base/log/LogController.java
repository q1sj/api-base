package com.xsy.base.log;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
     * @param names    文件名 多个逗号分割
     */
    @GetMapping("/get")
    public void get(HttpServletResponse response, @RequestParam String names) {
        File logFile = new File(logFileName);
        try (ServletOutputStream os = response.getOutputStream()) {
            List<File> fileList = new ArrayList<>();
            String delimiter = ",";
            for (String name : names.split(delimiter)) {
                String logPath = logFile.getParentFile().getPath() + "/" + name;
                fileList.add(new File(logPath));
            }
            byte[] bytes = zipFile(fileList);
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setContentLength(bytes.length);
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("log.zip", StandardCharsets.UTF_8.displayName()));
            os.write(bytes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static byte[] zipFile(List<File> files) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOut = new ZipOutputStream(byteArrayOutputStream);
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (File file : files) {
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                //内存中的映射文件
                RandomAccessFile raFile = new RandomAccessFile(file.getAbsoluteFile(), "r");
                MappedByteBuffer mappedByteBuffer = raFile.getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, raFile.length());
                writableByteChannel.write(mappedByteBuffer);
            }
        } catch (Exception e) {
            log.error("日志文件压缩失败 {}", e.getMessage(), e);
        }
        return byteArrayOutputStream.toByteArray();
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
            this.size = new BigDecimal(Objects.toString(file.length() / 1024 / 1024D)).setScale(2, RoundingMode.HALF_UP) + "MB";
            this.date = new Date(file.lastModified());
        }
    }
}
