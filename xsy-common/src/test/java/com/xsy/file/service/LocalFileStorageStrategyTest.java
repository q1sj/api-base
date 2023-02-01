package com.xsy.file.service;

import com.xsy.base.util.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LocalFileStorageStrategyTest {

    @Test
    public void saveFile() throws Exception {
        LocalFileStorageStrategy localFileStorageStrategy = new LocalFileStorageStrategy();
        localFileStorageStrategy.setBasePath("D:");
        String path = localFileStorageStrategy.saveFile(FileUtils.readFileToByteArray(new File("C:\\Users\\Q1sj\\Pictures\\33010001.jpg")), UUID.randomUUID() + ".jpg", "test222");
        localFileStorageStrategy.delete(path);
    }
}
