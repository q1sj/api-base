package com.xsy;

import com.xsy.base.util.FileUtils;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.ServletOutputStream;
import java.io.*;

/**
 * @author Q1sj
 * @date 2022.11.21 15:29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileServiceSpringTest {
    @Autowired
    private FileRecordService fileRecordService;

    @Test
    public void save() throws IOException {
        File file = new File("C:\\Users\\Q1sj\\Pictures\\1.png");
        FileRecordEntity recordEntity = fileRecordService.save(new FileInputStream(file), "1.png", "test", "", "", -1);
    }

    @Test
    public void getBytes() throws Exception {
        String path = "group1/M00/00/D1/wKgCGGOiriKAYs5DAACAhLcuCRc385.png";
//        byte[] fileBytes = fileRecordService.getFileBytes(path);
        InputStream inputStream = fileRecordService.getInputStream(path);
        try (FileOutputStream fos = new FileOutputStream("D:/" + System.currentTimeMillis() + ".png")) {
            IOUtils.copy(inputStream, fos);
        }

    }
}
