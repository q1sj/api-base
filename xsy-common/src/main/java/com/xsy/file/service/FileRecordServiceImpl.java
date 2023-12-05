package com.xsy.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xsy.base.util.BizAssertUtils;
import com.xsy.base.util.CollectionUtils;
import com.xsy.base.util.FileUtils;
import com.xsy.base.util.IpUtils;
import com.xsy.file.dao.FileRecordDao;
import com.xsy.file.entity.FileRecordDTO;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.entity.UploadFileDTO;
import com.xsy.security.user.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
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

/**
 * @author Q1sj
 * @date 2022.8.22 15:50
 */
@Slf4j
@Service
public class FileRecordServiceImpl extends ServiceImpl<FileRecordDao, FileRecordEntity> implements FileRecordService {
    private final List<String> illegalCharactersInDirectoryNames = Arrays.asList("*", ".", "\"", "[", "]", ":", ";", "|", "=");
    private final FileRecordDao fileRecordDao;
    private final FileStorageStrategy fileStorageStrategy;

    @Autowired(required = false)
    private HttpServletRequest request;

    public FileRecordServiceImpl(FileRecordDao fileRecordDao, FileStorageStrategy fileStorageStrategy) {
        this.fileRecordDao = fileRecordDao;
        this.fileStorageStrategy = fileStorageStrategy;
    }

    @Override
    public FileRecordEntity upload(UploadFileDTO uploadFileDTO) throws IOException {
        Objects.requireNonNull(uploadFileDTO.getFile());
        MultipartFile file = uploadFileDTO.getFile();
        long size = file.getSize();
        long maxSize = uploadFileDTO.getMaxSize();
        BizAssertUtils.isTrue(size <= maxSize, "文件过大 阈值:" + FileUtils.byteCountToDisplaySize(maxSize) + "实际:" + FileUtils.byteCountToDisplaySize(size));
        String originalFilename = file.getOriginalFilename();
        Set<String> fileExtension = uploadFileDTO.getFileExtension();
        BizAssertUtils.isTrue(CollectionUtils.isEmpty(fileExtension) || fileExtension.contains(FileUtils.getExtension(originalFilename).toLowerCase()), "文件类型不合法");
        try (InputStream is = file.getInputStream()) {
            return save(is, size, originalFilename, uploadFileDTO.getSource(), uploadFileDTO.getExpireMs());
        }
    }

    @Override
    public FileRecordEntity save(File file, String source, long expireMs) throws IOException {
        return save(Files.newInputStream(file.toPath()), file.length(), file.getName(), source, expireMs);
    }

    @Override
    public FileRecordEntity save(Long id, File file, String source, long expireMs) throws IOException {
        return save(id, Files.newInputStream(file.toPath()), file.length(), file.getName(), source, expireMs);
    }

    @Override
    public FileRecordEntity save(InputStream data, long fileSize, String originalFilename, String source, long expireMs) throws IOException {
        return save(IdWorker.getId(), data, fileSize, originalFilename, source, expireMs);
    }

    @Override
    public FileRecordEntity save(Long id, InputStream data, long fileSize, String originalFilename, String source, long expireMs) throws IOException {
        BizAssertUtils.isNotBlank(source, "source不能为空");
        // 判断source是否包含不允许字符
        illegalCharactersInDirectoryNames.forEach(s -> BizAssertUtils.isFalse(source.contains(s), "source中不允许出现的符号:" + s));
        // 持久化文件
        String path = fileStorageStrategy.saveFile(data, fileSize, generateFilename(originalFilename, source), source);
        FileRecordEntity fileRecordEntity = new FileRecordEntity();
        fileRecordEntity.setId(id);
        fileRecordEntity.setName(originalFilename);
        fileRecordEntity.setPath(path);
        fileRecordEntity.setFileType(FileUtils.getExtension(originalFilename));
        fileRecordEntity.setFileSize(fileSize);
        fileRecordEntity.setSource(source);
        fileRecordEntity.setUploadUserId(Objects.toString(SecurityUser.getUserId()));
        fileRecordEntity.setUploadIp(IpUtils.getIpAddr(request));
        fileRecordEntity.setUploadTime(new Date());
        fileRecordEntity.setDigest(fileStorageStrategy.digest(path));
        // expireMs <= 0 不过期
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
    public InputStream getInputStream(Long fileId) throws IOException {
        FileRecordEntity record = fileRecordDao.selectById(fileId);
        if (record == null) {
            throw new FileNotFoundException(Objects.toString(fileId));
        }
        return getInputStream(record.getPath());
    }

    @Override
    public FileRecordDTO getFileRecord(Long fileId) throws IOException {
        FileRecordEntity record = fileRecordDao.selectById(fileId);
        if (record == null) {
            throw new FileNotFoundException(Objects.toString(fileId));
        }
        return getFileRecord(record.getPath());
    }

    @Override
    public InputStream getInputStream(String path) throws IOException {
        return fileStorageStrategy.getInputStream(path);
    }

    @Override
    public boolean delete(String path) {
        log.info("delete {}", path);
        FileRecordEntity record = this.getRecordByPath(path);
        if (record == null) {
            log.warn("path:{} 不存在", path);
            return false;
        }
        try {
            fileStorageStrategy.delete(path);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            record.setRemark(e.getClass().getName() + ":" + e.getMessage());
            fileRecordDao.updateById(record);
        }
        return fileRecordDao.deleteById(record.getId()) > 0;
    }

    @Override
    public boolean delete(Long fileId) {
        log.info("delete {}", fileId);
        FileRecordEntity record = this.getById(fileId);
        if (record == null) {
            log.warn("id:{} 不存在", fileId);
            return false;
        }
        try {
            fileStorageStrategy.delete(record.getPath());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            record.setRemark(e.getClass().getName() + ":" + e.getMessage());
            fileRecordDao.updateById(record);
        }
        return fileRecordDao.deleteById(record.getId()) > 0;
    }

    @Override
    public void updateExpireTime(long fileId, long expireMs) {
        update(Wrappers.lambdaUpdate(FileRecordEntity.class)
                .eq(FileRecordEntity::getId, fileId)
                .set(FileRecordEntity::getExpireTime, expireMs > 0 ? new Date(System.currentTimeMillis() + expireMs) : null));
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
    @Scheduled(fixedDelay = 60 * 60 * 1000, initialDelay = 5 * 60 * 1000)
    public void deleteTask() {
        long start = System.currentTimeMillis();
        log.info("开始删除过期文件...");
        Date now = new Date();
        List<FileRecordEntity> expiredList = expiredList(now);
        int deleteFileCount = 0;
        for (FileRecordEntity fileRecordEntity : expiredList) {
            boolean delete = delete(fileRecordEntity.getPath());
            if (delete) {
                deleteFileCount++;
            }
        }
        log.info("结束删除过期文件...删除:{}个文件 耗时:{}ms", deleteFileCount, System.currentTimeMillis() - start);
    }

    /**
     * 生成存储文件名 业务名_时间_随机字符串.后缀名
     *
     * @param originalFilename 原始文件名
     * @param source
     * @return
     */
    private String generateFilename(String originalFilename, String source) {
        return source + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + "_" + UUID.randomUUID() + "." + FileUtils.getExtension(originalFilename);
    }
}
