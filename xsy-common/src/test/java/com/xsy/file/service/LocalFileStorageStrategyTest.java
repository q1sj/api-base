package com.xsy.file.service;

import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

public class LocalFileStorageStrategyTest {

    @Test
    public void saveFile() throws Exception {
        LocalFileStorageStrategy localFileStorageStrategy = new LocalFileStorageStrategy();
        localFileStorageStrategy.setBasePath("D:");
        File file = new File("C:\\Users\\Q1sj\\Pictures\\33010001.jpg");
        String path = localFileStorageStrategy.saveFile(Files.newInputStream(file.toPath()), file.length(), UUID.randomUUID() + ".jpg", "test222");
        localFileStorageStrategy.delete(path);
    }
}
