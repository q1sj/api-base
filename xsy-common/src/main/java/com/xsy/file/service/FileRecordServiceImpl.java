package com.xsy.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.util.*;
import com.xsy.file.dao.FileRecordDao;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.security.user.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2022.8.22 15:50
 */
@Slf4j
@Service
public class FileRecordServiceImpl implements FileRecordService {
    @Autowired(required = false)
    private HttpServletRequest request;

    private final List<String> illegalCharactersInDirectoryNames = Arrays.asList("*", ".", "\"", "[", "]", ":", ";", "|", "=");

    private final FileRecordDao fileRecordDao;

    private final FileStorageStrategy fileStorageStrategy;

    private final DelayQueue<DeleteFileDelayed> deleteFileDelayQueue = new DelayQueue<>();

    {
        String threadName = "delete-file-thread";
        log.info("init {}", threadName);
        new Thread(() -> {
            while (true) {
                try {
                    DeleteFileDelayed deleteFileDelayed = deleteFileDelayQueue.take();
                    this.delete(deleteFileDelayed.path);
                } catch (InterruptedException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }, threadName).start();
    }

    public FileRecordServiceImpl(FileRecordDao fileRecordDao, FileStorageStrategy fileStorageStrategy) {
        this.fileRecordDao = fileRecordDao;
        this.fileStorageStrategy = fileStorageStrategy;
    }

    @Override
    public FileRecordEntity upload(MultipartFile file, String source, long expireMs,
                                   long maxSize, List<String> fileExtension) throws IOException {
        long size = file.getSize();
        BizAssertUtils.isTrue(size <= maxSize, "文件过大 阈值:" + FileUtils.byteCountToDisplaySize(maxSize) + "实际:" + FileUtils.byteCountToDisplaySize(size));
        String originalFilename = file.getOriginalFilename();
        BizAssertUtils.isTrue(fileExtension.contains(getFileSuffix(originalFilename)), "后缀名不合法");
        return save(file.getBytes(), originalFilename, source, Objects.toString(SecurityUser.getUserId()), IpUtils.getIpAddr(request), expireMs);
    }

    @Override
    public FileRecordEntity save(InputStream data, String originalFilename, String source, String userId, String ip, long expireMs) throws IOException {
        BizAssertUtils.isNotBlank(source, "source不能为空");
        // 判断source是否包含不允许字符
        illegalCharactersInDirectoryNames.forEach(s -> BizAssertUtils.isFalse(source.contains(s), "source中不允许出现的符号:" + s));
        // 持久化文件
        int fileSize = data.available();
        String path = fileStorageStrategy.saveFile(data, generateFilename(originalFilename, source), source);
        FileRecordEntity fileRecordEntity = new FileRecordEntity();
        fileRecordEntity.setName(originalFilename);
        fileRecordEntity.setPath(path);
        fileRecordEntity.setFileType(getFileSuffix(originalFilename));
        fileRecordEntity.setFileSize(fileSize);
        fileRecordEntity.setSource(source);
        fileRecordEntity.setUploadUserId(userId);
        fileRecordEntity.setUploadIp(ip);
        fileRecordEntity.setDigest(fileStorageStrategy.digest(path));
        // expireMs < 0 不过期
        if (expireMs > 0) {
            Date expireTime = new Date(System.currentTimeMillis() + expireMs);
            fileRecordEntity.setExpireTime(expireTime);
        }
        // 持久化上传记录
        fileRecordDao.insert(fileRecordEntity);
        return fileRecordEntity;
    }

    public FileRecordEntity getRecordByPath(String path) {
        LambdaQueryWrapper<FileRecordEntity> wrapper = Wrappers.lambdaQuery(FileRecordEntity.class)
                .eq(FileRecordEntity::getPath, path);
        return fileRecordDao.selectOne(wrapper);
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        FileRecordEntity record = getRecordByPath(path);
        if (record == null) {
            throw new FileNotFoundException(path + " 不存在");
        }
        InputStream inputStream = fileStorageStrategy.getInputStream(path);
        String recordDigest = record.getDigest();
        String nowDigest = fileStorageStrategy.digest(path);
        Integer recordFileSize = record.getFileSize();
        int nowFileSize = inputStream.available();
        if (!Objects.equals(recordDigest, nowDigest)
                || !Objects.equals(recordFileSize, nowFileSize)) {
            throw new IOException(String.format("%s文件已损坏 recordDigest:%s now:%s recordFileSize:%s now:%s", path, recordDigest, nowDigest, recordFileSize, nowFileSize));
        }
        return inputStream;
    }

    @Override
    public boolean delete(String path) {
        log.info("delete {}", path);
        try {
            fileStorageStrategy.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            FileRecordEntity recordEntity = fileRecordDao.selectOne(Wrappers.lambdaQuery(FileRecordEntity.class).eq(FileRecordEntity::getPath, path));
            recordEntity.setRemark(e.getMessage());
            fileRecordDao.updateById(recordEntity);
            return false;
        }
        return fileRecordDao.delete(Wrappers.lambdaQuery(FileRecordEntity.class).eq(FileRecordEntity::getPath, path)) > 0;
    }

    /**
     * 过期文件记录
     *
     * @return
     */
    private List<FileRecordEntity> expiredList() {
        LambdaQueryWrapper<FileRecordEntity> wrapper = Wrappers.lambdaQuery(FileRecordEntity.class)
                .ne(FileRecordEntity::getRemark, null)
                .lt(FileRecordEntity::getExpireTime, new Date());
        return fileRecordDao.selectList(wrapper);
    }

    /**
     * 定时删除过期
     */
    @Scheduled(cron = "${file.delete-task.cron:0 0 1 * * ?}")
    public void deleteTask() {
        long start = System.currentTimeMillis();
        log.info("开始删除过期文件...");
        // 删除已过期文件(防止因为重启延迟队列任务丢失)
        List<FileRecordEntity> expiredList = expiredList();
        for (FileRecordEntity fileRecordEntity : expiredList) {
            delete(fileRecordEntity.getPath());
        }
        // 24小时内过期文件加入延迟队列
        LambdaQueryWrapper<FileRecordEntity> wrapper = Wrappers.lambdaQuery(FileRecordEntity.class)
                .lt(FileRecordEntity::getExpireTime, DateUtils.addHours(new Date(), 24));
        List<FileRecordEntity> list = this.fileRecordDao.selectList(wrapper);
        list.forEach(f -> deleteFileDelayQueue.add(new DeleteFileDelayed(f.getPath(), f.getExpireTime())));
        log.info("结束删除过期文件...耗时:{}ms", System.currentTimeMillis() - start);
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    private String getFileSuffix(String fileName) {
        /**
         * 未知文件类型
         */
        String unknownFileType = "unknown";
        if (StringUtils.isBlank(fileName)) {
            return unknownFileType;
        }
        String[] split = fileName.split("\\.");
        if (split.length > 1) {
            return split[split.length - 1];
        }
        return unknownFileType;
    }

    /**
     * 生成存储文件名 业务名_时间戳_随机字符串.后缀名
     *
     * @param originalFilename 原始文件名
     * @param source
     * @return
     */
    private String generateFilename(String originalFilename, String source) {
        return source + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + getFileSuffix(originalFilename);
    }

    @AllArgsConstructor
    private static class DeleteFileDelayed implements Delayed {

        private final String path;
        private final Date expireTime;

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireTime.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            TimeUnit timeUnit = TimeUnit.MILLISECONDS;
            return Long.compare(this.getDelay(timeUnit), o.getDelay(timeUnit));
        }
    }
}
