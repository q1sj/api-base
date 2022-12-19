package com.xsy.api.demo.controller;

import com.xsy.security.annotation.NoAuth;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Q1sj
 * @date 2022.12.16 9:42
 */
@RestController
public class BigFileController {

    @NoAuth
    @GetMapping("/download")
    public void download(@RequestParam String path, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        try (ServletOutputStream os = response.getOutputStream();
             FileInputStream fis = new FileInputStream(path);
        ) {
            IOUtils.copy(fis, os);
            os.flush();
        }
    }

    @NoAuth
    @GetMapping("/download2")
    public void download2(@RequestParam String path, HttpServletResponse response) throws IOException {
        try (ServletOutputStream os = response.getOutputStream()) {
            //OutOfMemoryError: Direct buffer memory
            os.write(Files.readAllBytes(Paths.get(path)));
        }
    }

    @NoAuth
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
        file.transferTo(new File("D:/test"));
    }
}
