package com.xsy.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2022.9.7 16:58
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     * 获取年月日 时分秒毫秒清零
     *
     * @param datetime
     * @return
     */
    public static Date getDate(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(sdf.format(datetime));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取小时 24小时制
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        return Integer.parseInt(sdf.format(date));
    }
}
