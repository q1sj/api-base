package com.xsy;

import com.xsy.base.util.FileUtils;
import com.xsy.file.entity.FileRecordEntity;
import com.xsy.file.service.FileRecordService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

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
        FileRecordEntity recordEntity = fileRecordService.save(FileUtils.readFileToByteArray(file), "1.png", "test", "", "", -1);
    }

    @Test
    public void getBytes() throws Exception {
        byte[] fileBytes = fileRecordService.getFileBytes("/test/20221121/test_1669015927560_ab31c04c-9432-4fe8-b1ab-5f99e177ed32.png");
    }
}
