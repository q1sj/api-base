package com.xsy.base.util;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
     * 要删除的文件扩展名,删除所有扩展名使用{@link Collections#emptyList()}
     * foo.txt      --> "txt"
     * a/b/c.jpg    --> "jpg"
     * a/b.txt/c    --> ""
     * a/b/c        --> ""
     */
    private final List<String> extensionList;
    /**
     * 允许最大使用率 默认85
     * 此参数优先级大于{@link #cleanDayAgo}
     * 使用率大于此值会继续删除
     */
    @Setter
    private int allowMaxUtilizationRate;

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

        for (int cleanDayAgo = this.cleanDayAgo; cleanDayAgo > 0; cleanDayAgo--) {
            log.info("{} 删除{}天前数据 扩展名:{}", path, cleanDayAgo, extensionList.isEmpty() ? "*" : extensionList);
            clean(path, cleanDayAgo);
            long usableSpace = path.getUsableSpace();
            int utilizationRate = utilizationRate();
            log.info("{} 可用空间:{} 使用率:{}% 允许最大使用率:{}%", path, FileUtils.byteCountToDisplaySize(usableSpace), utilizationRate, allowMaxUtilizationRate);
            if (allowMaxUtilizationRate > utilizationRate) {
                return;
            }
        }
    }

    private int utilizationRate() {
        if (!path.exists()) {
            log.info("目录{}不存在使用率为0", path.getAbsolutePath());
            return 0;
        }
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
        LocalDateTime lastModifiedTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault());
        LocalDateTime today = LocalDate.now().atStartOfDay();
        if (today.plusDays(-cleanDayAgo).isAfter(lastModifiedTime)) {
            if (extensionList.isEmpty() || extensionList.contains(FilenameUtils.getExtension(file.getName()))) {
                log.warn("delete lastModified:{} {}", lastModifiedTime, file);
                FileUtils.deleteQuietly(file);
            }
        }
    }
}
