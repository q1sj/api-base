package com.xsy.api.common.config;

import com.xsy.base.util.FileUtils;
import com.xsy.base.util.IOUtils;
import com.xsy.security.annotation.NoAuth;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Q1sj
 * @date 2022.12.12 9:20
 */
@RestController
@RequestMapping("/test/resource")
public class TestReadResourceController {
    @NoAuth
    @GetMapping("/read")
    public void read(String path, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding("UTF-8");
        String[] split = path.replace("\\", "/").split("/");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(split[split.length - 1], StandardCharsets.UTF_8.displayName()));
        try (InputStream is = FileUtils.readResourceFile(path);
             ServletOutputStream os = response.getOutputStream()) {
            IOUtils.copy(is, os);
        }
    }
}
