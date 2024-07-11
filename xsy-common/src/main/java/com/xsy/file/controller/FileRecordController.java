package com.xsy.file.controller;

import com.xsy.base.exception.GlobalException;
import com.xsy.base.util.FileUtils;
import com.xsy.base.util.IOUtils;
import com.xsy.base.util.Result;
import com.xsy.file.entity.FileRecordDTO;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.entity.UploadFileDTO;
import com.xsy.file.service.FileRecordService;
import com.xsy.security.annotation.NoAuth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    public static final String DOWNLOAD_MAPPING = "/download";
	public static final String IMG_MAPPING = "/img";
	@Autowired
	private FileRecordService fileRecordService;
	/**
	 * 图片接口浏览器缓存天数
	 * 默认2592000秒(30天)
	 */
	@Value("${file.img.max-age:2592000}")
	private Integer maxAge;

	/**
	 * 上传文件
	 *
	 * @param file
	 * @return
	 * @apiNote demo 根据具体业务单独编写接口 设置文件大小阈值,合法后缀名
	 */
    @PostMapping("/upload")
    public Result<FileRecordEntity> upload(MultipartFile file) {
        String source = "upload-api-demo";
        UploadFileDTO uploadFileDTO = new UploadFileDTO()
                .setFile(file)
                .setFileExtension(UploadFileDTO.IMAGE_FILE_EXTENSION)
                .setExpireMs(TimeUnit.DAYS.toMillis(1))
                .setMaxSize(10 * FileUtils.ONE_MB)
                .setSource(source);
        try {
            FileRecordEntity record = fileRecordService.upload(uploadFileDTO);
            return Result.ok(record);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return Result.error("上传失败");
        }
    }

    /**
     * 下载文件(根据path)
     *
     * @param response
     * @param path     {@link FileRecordEntity#getPath()}
     */
    @NoAuth
    @GetMapping(DOWNLOAD_MAPPING)
    public void download(HttpServletResponse response, @RequestParam String path) {
	    response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
	    try {
		    FileRecordDTO fileRecord = this.fileRecordService.getFileRecord(path);
		    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileRecord.getName(), StandardCharsets.UTF_8.displayName()));
		    try (InputStream is = fileRecord.getContent();
		         OutputStream os = response.getOutputStream()) {
			    response.setContentLengthLong(fileRecord.getFileSize());
			    IOUtils.copyLarge(is, os);
		    }
	    } catch (FileNotFoundException e) {
		    throw new GlobalException("文件已过期或不存在", e);
	    } catch (IOException e) {
		    throw new GlobalException("文件下载失败", e);
	    }
    }

	/**
	 * 下载文件(根据id)
	 *
	 * @param response
	 * @param fileId
	 */
	@NoAuth
	@GetMapping(DOWNLOAD_MAPPING + "/{fileId}")
	public void download(HttpServletResponse response, @PathVariable Long fileId) {
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		try {
			FileRecordDTO fileRecord = this.fileRecordService.getFileRecord(fileId);
			response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileRecord.getName(), StandardCharsets.UTF_8.displayName()));
			try (InputStream is = fileRecord.getContent();
			     OutputStream os = response.getOutputStream()) {
				response.setContentLengthLong(fileRecord.getFileSize());
				IOUtils.copyLarge(is, os);
			}
		} catch (FileNotFoundException e) {
			throw new GlobalException("文件已过期或不存在", e);
		} catch (IOException e) {
			throw new GlobalException("文件下载失败", e);
		}
	}

	/**
	 * 访问图片(根据id)
	 *
	 * @param response
	 * @param fileId
	 */
	@NoAuth
	@GetMapping(IMG_MAPPING + "/{fileId}")
	public void img(HttpServletResponse response, @PathVariable Long fileId) {
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			FileRecordDTO fileRecord = this.fileRecordService.getFileRecord(fileId);
			try (InputStream is = fileRecord.getContent();
			     OutputStream os = response.getOutputStream()) {
				response.setHeader("Cache-Control", "max-age=" + maxAge + ", must-revalidate");
				response.setContentLengthLong(fileRecord.getFileSize());
				IOUtils.copy(is, os);
			}
		} catch (FileNotFoundException e) {
			throw new GlobalException("文件已过期或不存在", e);
		} catch (IOException e) {
			throw new GlobalException("文件下载失败", e);
		}
	}

	/**
	 * 访问图片(根据path)
	 *
	 * @param response
	 * @param path
	 */
	@NoAuth
	@GetMapping(IMG_MAPPING)
	public void img(HttpServletResponse response, @RequestParam String path) {
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		try {
			FileRecordDTO fileRecord = fileRecordService.getFileRecord(path);
			try (InputStream is = fileRecord.getContent();
			     OutputStream os = response.getOutputStream()) {
				response.setHeader("Cache-Control", "max-age=" + maxAge + ", must-revalidate");
				response.setContentLengthLong(fileRecord.getFileSize());
				IOUtils.copy(is, os);
			}
		} catch (FileNotFoundException e) {
			throw new GlobalException("文件已过期或不存在", e);
		} catch (IOException e) {
			throw new GlobalException("文件下载失败", e);
		}
	}


	/**
	 * 删除
	 *
	 * @param path
	 * @return
	 */
	@PostMapping("/delete")
	public Result<Void> delete(@RequestParam String path) {
		boolean delete = fileRecordService.delete(path);
		return delete ? Result.ok() : Result.error("删除失败");
	}
}

