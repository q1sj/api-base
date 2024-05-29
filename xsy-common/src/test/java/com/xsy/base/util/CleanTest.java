package com.xsy.base.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class CleanTest {
    Clean clean;

    @Before
    public void setUp() throws Exception {
        String property = System.getProperty("java.io.tmpdir");
        File tempDir = new File(property);
        File testCleanDir = new File(tempDir, "testClean");
        testCleanDir.mkdirs();
        new File(testCleanDir, "testDir").mkdirs();
        File file1 = new File(testCleanDir, "aaa.txt");
        file1.createNewFile();
        file1.setLastModified(1L);
        File file2 = new File(testCleanDir, "ccc.jpg");
        file2.createNewFile();
        file2.setLastModified(1L);
        new File(testCleanDir, "bbb.txt").createNewFile();

        clean = new Clean(testCleanDir.getAbsolutePath(), 10, Collections.singletonList("txt"));
        clean.setAllowMaxUtilizationRate(91);
    }

    @After
    public void after() throws Exception {
        String property = System.getProperty("java.io.tmpdir");
        File tempDir = new File(property);
        File testCleanDir = new File(tempDir, "testClean");
        FileUtils.deleteDirectory(testCleanDir);
    }

    @Test
    public void start() {
        clean.start();
        assertEquals(2, clean.getPath().listFiles().length);
    }
}