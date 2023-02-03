package com.xsy.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xsy.base.util.*;
import com.xsy.file.dao.FileRecordDao;
import com.xsy.file.entity.FileRecordDTO;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.entity.UploadFileDTO;
import com.xsy.security.user.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
    public FileRecordEntity upload(UploadFileDTO uploadFileDTO) throws IOException {
        MultipartFile file = uploadFileDTO.getFile();
        long size = file.getSize();
        long maxSize = uploadFileDTO.getMaxSize();
        BizAssertUtils.isTrue(size <= maxSize, "文件过大 阈值:" + FileUtils.byteCountToDisplaySize(maxSize) + "实际:" + FileUtils.byteCountToDisplaySize(size));
        String originalFilename = file.getOriginalFilename();
        List<String> fileExtension = uploadFileDTO.getFileExtension();
        BizAssertUtils.isTrue(CollectionUtils.isEmpty(fileExtension) || fileExtension.contains(FileUtils.getExtension(originalFilename)), "文件类型不合法");
        try (InputStream is = file.getInputStream()) {
            return save(is, size, originalFilename, uploadFileDTO.getSource(), uploadFileDTO.getExpireMs());
        }
    }

    @Override
    public FileRecordEntity save(File file, String source, long expireMs) throws IOException {
        return save(Files.newInputStream(file.toPath()), file.length(), file.getName(), source, expireMs);
    }

    @Override
    public FileRecordEntity save(InputStream data, long fileSize, String originalFilename, String source, long expireMs) throws IOException {
        BizAssertUtils.isNotBlank(source, "source不能为空");
        // 判断source是否包含不允许字符
        illegalCharactersInDirectoryNames.forEach(s -> BizAssertUtils.isFalse(source.contains(s), "source中不允许出现的符号:" + s));
        // 持久化文件
        String path = fileStorageStrategy.saveFile(data, fileSize, generateFilename(originalFilename, source), source);
        FileRecordEntity fileRecordEntity = new FileRecordEntity();
        fileRecordEntity.setName(originalFilename);
        fileRecordEntity.setPath(path);
        fileRecordEntity.setFileType(FileUtils.getExtension(originalFilename));
        fileRecordEntity.setFileSize(fileSize);
        fileRecordEntity.setSource(source);
        fileRecordEntity.setUploadUserId(Objects.toString(SecurityUser.getUserId()));
        fileRecordEntity.setUploadIp(IpUtils.getIpAddr(request));
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
    public FileRecordDTO getFileRecord(String path) throws IOException {
        FileRecordEntity record = getRecordByPath(path);
        if (record == null) {
            throw new FileNotFoundException(path + " 不存在");
        }
        InputStream inputStream = fileStorageStrategy.getInputStream(path);
        FileRecordDTO dto = new FileRecordDTO();
        BeanUtils.copyProperties(record, dto);
        dto.setContent(inputStream);
        return dto;
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return fileStorageStrategy.getInputStream(path);
    }

    @Override
    public boolean delete(String path) {
        log.info("delete {}", path);
        try {
            fileStorageStrategy.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            FileRecordEntity record = fileRecordDao.selectOne(Wrappers.lambdaQuery(FileRecordEntity.class).eq(FileRecordEntity::getPath, path));
            if (record != null) {
                record.setRemark(e.getMessage());
                fileRecordDao.updateById(record);
            }
            return false;
        }
        return fileRecordDao.delete(Wrappers.lambdaQuery(FileRecordEntity.class).eq(FileRecordEntity::getPath, path)) > 0;
    }

    /**
     * 过期文件记录
     *
     * @param expireTime 过期时间小于此时间的
     * @return
     */
    private List<FileRecordEntity> expiredList(Date expireTime) {
        LambdaQueryWrapper<FileRecordEntity> wrapper = Wrappers.lambdaQuery(FileRecordEntity.class)
                .isNull(FileRecordEntity::getRemark)
                .lt(FileRecordEntity::getExpireTime, expireTime);
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
        Date now = new Date();
        List<FileRecordEntity> expiredList = expiredList(now);
        for (FileRecordEntity fileRecordEntity : expiredList) {
            delete(fileRecordEntity.getPath());
        }
        // 24小时内过期文件加入延迟队列
        List<FileRecordEntity> after24HourExpiredList = this.expiredList(DateUtils.addHours(now, 24));
        after24HourExpiredList.forEach(f -> deleteFileDelayQueue.add(new DeleteFileDelayed(f.getPath(), f.getExpireTime())));
        log.info("结束删除过期文件...耗时:{}ms", System.currentTimeMillis() - start);
    }

    /**
     * 生成存储文件名 业务名_时间戳_随机字符串.后缀名
     *
     * @param originalFilename 原始文件名
     * @param source
     * @return
     */
    private String generateFilename(String originalFilename, String source) {
        return source + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "." + FileUtils.getExtension(originalFilename);
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
