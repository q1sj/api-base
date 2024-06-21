package com.xsy.file.service;

import com.xsy.base.util.DateFormatUtils;
import com.xsy.base.util.DigestUtils;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2024/6/20 上午9:26
 */
@Slf4j
public class MinioFileStorageStrategy implements FileStorageStrategy {

	private final String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucketName;

	private MinioClient minioClient;

	public MinioFileStorageStrategy(String endpoint, String accessKey, String secretKey, String bucketName) {
		this.endpoint = endpoint;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.bucketName = bucketName;
		this.minioClient = MinioClient.builder()
				.endpoint(endpoint)
				.credentials(accessKey, secretKey)
				.build();
	}

	@PostConstruct
	public void init() {
		try {
			boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!found) {
				minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
			} else {
				log.info("minio bucket '{}' already exists.", bucketName);
			}
		} catch (Exception e) {
			log.error("minio创建桶:{}失败 请手动创建", bucketName, e);
		}
	}

	@Override
	public String digest(String path) throws IOException {
		try (InputStream inputStream = getInputStream(path)) {
			return DigestUtils.md5Hex(inputStream);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public InputStream getInputStream(String path) throws IOException {
		try {
			return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(path).build());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public String saveFile(InputStream data, long length, String fileName, String source) throws IOException {
		String objectName = getObjectName(fileName, source);
		PutObjectArgs putObjectArgs = PutObjectArgs.builder()
				.bucket(bucketName)
				.stream(data, length, -1)
				.object(objectName).build();
		try {
			minioClient.putObject(putObjectArgs);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
		return objectName;
	}

	@Override
	public void delete(String path) throws IOException {
		try {
			minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(path).build());
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	private String getObjectName(String filename, String source) {
		return "/" + source + "/" + DateFormatUtils.format(new Date(), "yyyyMMdd") + "/" + filename;
	}
}
