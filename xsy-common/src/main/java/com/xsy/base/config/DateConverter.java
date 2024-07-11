package com.xsy.base.config;

import com.xsy.base.util.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期转换
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class DateConverter implements Converter<String, Date> {
    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);
    private static List<String> formatList = new ArrayList<>(5);
    static {
        formatList.add("yyyy-MM");
        formatList.add("yyyy-MM-dd");
        formatList.add("yyyy-MM-dd HH:mm");
        formatList.add("yyyy-MM-dd HH:mm:ss");
        formatList.add("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    }

    @Override
    public Date convert(String source) {
        String value = source.trim();
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}.*T.*\\d{1,2}:\\d{1,2}:\\d{1,2}.*..*$")) {
            return parseDate(source, formatList.get(4));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formatList.get(3));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formatList.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formatList.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formatList.get(0));
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    /**
     * 格式化日期
     * @param dateStr String 字符型日期
     * @param format String 格式
     * @return Date 日期
     */
    public Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            date = DateFormatUtils.parse(dateStr, format);
        } catch (Exception e) {
            logger.error("Formatted date with date: {} and format : {} ", dateStr, format);
        }
        return date;
    }
}
