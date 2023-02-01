package com.xsy.base.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @author Q1sj
 * @date 2022.9.7 16:58
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    /**
     * 获取年月日 时分秒毫秒清零
     *
     * @param datetime
     * @return
     */
    public static Date getDate(Date datetime) {
        return toDate(toLocalDate(datetime));
    }

    public static int getYear(Date date) {
        return toZonedDateTime(date).getYear();
    }

    public static int getMonth(Date date) {
        return toZonedDateTime(date).getMonthValue();
    }

    public static int getDayOfMonth(Date date) {
        return toZonedDateTime(date).getDayOfMonth();
    }

    /**
     * 获取小时 24小时制
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        return toZonedDateTime(date).getHour();
    }

    public static Date toDate(LocalDate localDate) {
        return toDate(localDate.atStartOfDay());
    }

    public static Date toDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZONE_ID);
        return Date.from(zonedDateTime.toInstant());
    }

    private static ZonedDateTime toZonedDateTime(Date date) {
        return date.toInstant().atZone(ZONE_ID);
    }

    public static LocalDate toLocalDate(Date date) {
        return toZonedDateTime(date).toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return toZonedDateTime(date).toLocalDateTime();
    }
}
