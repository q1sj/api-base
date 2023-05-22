package com.xsy.base.util;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

/**
 * @author Q1sj
 * @date 2023.5.11 14:42
 */
@Slf4j
@Getter
public class Clean {
    private final static Object LOCK = new Object();
    /**
     * 清理目录
     */
    private final File path;
    /**
     * 清理几天前的数据
     */
    private final int cleanDayAgo;
    /**
     * 允许最大使用率 默认85
     * 此参数优先级大于{@link #cleanDayAgo}
     * 使用率大于此值会继续删除
     */
    @Setter
    private int allowMaxUtilizationRate;
    /**
     * 要删除的文件扩展名
     */
    private final List<String> extensionList;

    public Clean(String path, int cleanDayAgo) {
        this(path, cleanDayAgo, Collections.emptyList());
    }

    public Clean(String path, int cleanDayAgo, List<String> extensionList) {
        if (cleanDayAgo <= 0) {
            throw new IllegalArgumentException();
        }
        this.allowMaxUtilizationRate = 85;
        this.path = new File(path);
        this.cleanDayAgo = cleanDayAgo;
        this.extensionList = Collections.unmodifiableList(extensionList);
    }

    @Synchronized("LOCK")
    public void start() {
        if (!this.path.exists() || !this.path.isDirectory()) {
            log.warn("目录{}不存在", this.path.getAbsolutePath());
            return;
        }
        int cleanDayAgo = this.cleanDayAgo;
        log.info("{} 删除{}天前数据 扩展名:{}", path, cleanDayAgo, extensionList.isEmpty() ? "*" : extensionList);
        clean(path, cleanDayAgo);
        long usableSpace = path.getUsableSpace();
        int utilizationRate = utilizationRate();
        log.info("{} 可用空间:{} 使用率:{}% 允许最大使用率:{}%", path, FileUtils.byteCountToDisplaySize(usableSpace), utilizationRate, allowMaxUtilizationRate);
        while (allowMaxUtilizationRate < utilizationRate && cleanDayAgo > 0) {
            if (!this.path.exists()) {
                log.warn("目录{}不存在", this.path.getAbsolutePath());
                return;
            }
            cleanDayAgo--;
            log.info("{} 删除{}天前数据 扩展名:{}", path, cleanDayAgo, extensionList.isEmpty() ? "*" : extensionList);
            clean(path, cleanDayAgo);
            utilizationRate = utilizationRate();
            log.info("{} 可用空间:{} 使用率:{}% 允许最大使用率:{}%", path, FileUtils.byteCountToDisplaySize(usableSpace), utilizationRate, allowMaxUtilizationRate);
        }
    }

    private int utilizationRate() {
        double rate = 100D * (path.getTotalSpace() - path.getUsableSpace()) / path.getTotalSpace();
        return (int) rate;
    }

    private void clean(File file, int cleanDayAgo) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                log.warn("删除空目录 {}", file.getAbsolutePath());
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
            if (extensionList.isEmpty() || extensionList.contains(FileUtils.getExtension(file.getName()))) {
                log.warn("delete lastModified:{} {}", localDateTime, file);
                FileUtils.deleteQuietly(file);
            }
        }
    }
}
