package com.xsy.base.util;

import com.xsy.base.log.IgnoreLog;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtilsTest {

    @Test
    public void toLogJsonString() {
        Body body = new Body();
        body.setCode(0);
        body.setData("aaa");
        TestObj testObj = new TestObj();
        testObj.body = body;
        // 对象嵌套
        String s1 = JsonUtils.toLogJsonString(testObj);
        String s2 = JsonUtils.toJsonString(testObj);
        Assert.assertTrue(s1.length() < s2.length());
        // 集合
        List<TestObj> list = Arrays.asList(testObj, testObj);
        String s3 = JsonUtils.toLogJsonString(list);
        String s4 = JsonUtils.toJsonString(list);
        Assert.assertTrue(s3.length() < s4.length());
        // 数组
        Object[] arr = new Object[2];
        arr[0] = testObj;
        arr[1] = testObj;
        String s5 = JsonUtils.toLogJsonString(arr);
        String s6 = JsonUtils.toJsonString(arr);
        Assert.assertTrue(s5.length() < s6.length());
    }

    @Test
    public void testLongLogString() {
        Map map = new HashMap();
        StringBuilder longLogString = new StringBuilder();
        for (int i = 0; i < JsonUtils.maxLogStringLength; i++) {
            longLogString.append("a");
        }
        map.put("a", 1);
        map.put("b", longLogString + "111111111111111111111111111111111111");
        String logJsonString = JsonUtils.toLogJsonString(map);
        String jsonString = JsonUtils.toJsonString(map);
        Assert.assertTrue(logJsonString.length() < jsonString.length());


        Body body = new Body();
        body.setData2(longLogString + "11111111111111111111111111111111");
        String logJsonString2 = JsonUtils.toLogJsonString(body);
        String jsonString2 = JsonUtils.toJsonString(body);
        Assert.assertTrue(logJsonString2.length() < jsonString2.length());
    }

    @Data
    public static class TestObj {
        private Body body;
    }

    @Data
    public static class Body {
        private Integer code;
        @IgnoreLog
        private String data;

        private String data2;
    }
}