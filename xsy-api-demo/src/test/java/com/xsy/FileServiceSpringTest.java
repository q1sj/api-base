package com.xsy;

import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

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
        FileRecordEntity recordEntity = fileRecordService.save(Files.newInputStream(file.toPath()), file.length(), "1.png", "test", -1);
    }

    @Test
    public void getBytes() throws Exception {
        String path = "/test/20221222/test_1671679252247_8ccef928-a222-47e5-90f6-562f03f3ff3e.png";
//        byte[] fileBytes = fileRecordService.getFileBytes(path);
        InputStream inputStream = fileRecordService.getInputStream(path);
        try (FileOutputStream fos = new FileOutputStream("D:/" + System.currentTimeMillis() + ".png")) {
            IOUtils.copy(inputStream, fos);
        }

    }
}
