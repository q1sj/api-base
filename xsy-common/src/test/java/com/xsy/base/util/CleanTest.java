package com.xsy.base.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static org.junit.Assert.assertEquals;

public class CleanTest {
    Clean clean;

    @Before
    public void setUp() throws Exception {
        String property = System.getProperty("java.io.tmpdir");
        File tempDir = new File(property);
        File testCleanDir = new File(tempDir, "testClean");
        testCleanDir.mkdirs();
        File file1 = new File(testCleanDir, "aaa.txt");
        file1.createNewFile();
        file1.setLastModified(1L);
        new File(testCleanDir, "bbb.txt").createNewFile();
        clean = new Clean(testCleanDir.getAbsolutePath(), 1, FileUtils.ONE_GB);
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
        assertEquals(1, clean.getPath().listFiles().length);
    }

    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                if (inetAddress instanceof Inet4Address) {
                    String hostAddress = inetAddress.getHostAddress();
                    System.out.println(hostAddress);
                }
            }
        }
    }
}