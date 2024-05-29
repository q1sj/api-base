package com.xsy.sys.service.impl;

import com.xsy.sys.dao.SysConfigDao;
import com.xsy.sys.entity.BooleanKey;
import com.xsy.sys.entity.IntKey;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SysConfigServiceImplTest {

    @Spy
    @InjectMocks
    private SysConfigServiceImpl sysConfigService;
    @Mock
    private SysConfigDao sysConfigDao;

    String startSymbol = "${";
    String endSymbol = "}";
    Map<String, String> map = new HashMap<>();

    {
        map.put("xxx_api", "http://${ip}:${port}/api/xxx");
        map.put("aaa_api", "http://${host}/api/aaa");
        map.put("host", "${ip}:${port}");
        map.put("ip", "192.168.1.1");
        map.put("port", "8080");
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test() {
        String xxx_api = getValue("xxx_api");
        String aaa_api = getValue("aaa_api");
    }

    @Test
    public void testRegex() {
        String val = "http://${ip}:${port}/api/xxx";
        Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
        Matcher matcher = pattern.matcher(val);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
        }
    }

    @Test
    public void testPropertyPlaceholderHelper() {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", false);
        PropertyPlaceholderHelper.PlaceholderResolver resolver = map::get;
        String str = helper.replacePlaceholders(map.get("aaa_api"), resolver);
        String str2 = helper.replacePlaceholders("${abc:123}", resolver);
    }

    @Test
    public void testGet() {
        BooleanKey xxxEnableKey = new BooleanKey("XXX_ENABLE", true);
        Boolean xxxEnable = sysConfigService.get(xxxEnableKey);
        Assert.assertTrue(xxxEnable);

        IntKey xxxKey = new IntKey("XXX", 99);
        Integer xxx = sysConfigService.get(xxxKey);
        Assert.assertEquals(xxx, Integer.valueOf(99));
    }

    public String getValue(String key) {
        return parseExpression(getOriginalValue(key));
    }

    private String parseExpression(String val) {
        for (int fromIndex = 0; fromIndex < val.length(); fromIndex++) {
            int startIndex = val.indexOf(startSymbol, fromIndex);
            int endIndex = val.indexOf(endSymbol, fromIndex);
            if (startIndex < 0 || endIndex < 1) {
                // 不存在表达式
                break;
            }
            // 截取${}中内容
            String expression = val.substring(startIndex + startSymbol.length(), endIndex);
            String expressionValue = parseExpression(getOriginalValue(expression));
            val = val.replace(startSymbol + expression + endSymbol, expressionValue);
            fromIndex = startIndex + expressionValue.length() - 1;
        }
        return val;
    }

    private String getOriginalValue(String key) {
        String val = map.get(key);
        if (val == null) {
            return "";
        }
        return val;
    }
}
