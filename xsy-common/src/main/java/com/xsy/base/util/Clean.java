package com.xsy.base.util;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Q1sj
 * @date 2023.5.11 14:42
 */
@Slf4j
@Getter
public class Clean {
    private final static Object LOCK = new Object();
    private final File path;
    private final int cleanDayAgo;
    private final long reservedSpaceSize;

    public Clean(String path, int cleanDayAgo, long reservedSpaceSize) {
        if (cleanDayAgo <= 0 && reservedSpaceSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.path = new File(path);
        this.cleanDayAgo = cleanDayAgo;
        this.reservedSpaceSize = reservedSpaceSize;
    }

    @Synchronized("LOCK")
    public void start() {
        if (!this.path.exists() || !this.path.isDirectory()) {
            log.warn("目录{}不存在", this.path.getAbsolutePath());
            return;
        }
        int cleanDayAgo = this.cleanDayAgo;
        log.info("{} 删除{}天前数据", path, cleanDayAgo);
        clean(path, cleanDayAgo);
        long usableSpace = path.getUsableSpace();
        log.info("{} 需要预留空间:{} 可用空间:{}", path, FileUtils.byteCountToDisplaySize(reservedSpaceSize), FileUtils.byteCountToDisplaySize(usableSpace));
        while (path.getUsableSpace() < reservedSpaceSize && cleanDayAgo > 0) {
            if (!this.path.exists()) {
                log.warn("目录{}不存在", this.path.getAbsolutePath());
                return;
            }
            cleanDayAgo--;
            log.info("{} 预留空间不足 删除{}天前数据", path, cleanDayAgo);
            clean(path, cleanDayAgo);
            log.info("{} 需要预留空间:{} 可用空间:{}", path, FileUtils.byteCountToDisplaySize(reservedSpaceSize), FileUtils.byteCountToDisplaySize(usableSpace));
        }
    }

    private void clean(File file, int cleanDayAgo) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                log.info("删除空目录 {}", file.getAbsolutePath());
                FileUtils.deleteQuietly(file);
                return;
            }
            for (File f : files) {
                clean(f, cleanDayAgo);
            }
            return;
        }
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());
        LocalDateTime now = LocalDate.now().atStartOfDay();
        if (now.plusDays(-cleanDayAgo).isAfter(localDateTime)) {
            log.info("lastModified:{} delete {}", localDateTime, file);
            FileUtils.deleteQuietly(file);
        }
    }
}
