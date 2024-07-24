package com.xsy.base.util;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Q1sj
 * @date 2022.9.7 16:58
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final String DEFAULT_NTP_SERVER_HOST = "time.windows.com";
    public static final int DEFAULT_NTP_SERVER_PORT = 123;

    public static final ZoneId ZONE_ID = ZoneId.systemDefault();

    public static Date getNetDate() throws IOException {
        return getNetDate(DEFAULT_NTP_SERVER_HOST);
    }

    /**
     * 持续分钟
     *
     * @param start
     * @param end
     * @return
     */
    public static long durationMinute(Date start, Date end) {
        return duration(start, end, TimeUnit.MINUTES);
    }

    /***
     * 持续时间
     * @param start 开始时间
     * @param end 结束时间
     * @param timeUnit 返回的持续时间单位
     * @return
     */
    public static long duration(Date start, Date end, TimeUnit timeUnit) {
        return timeUnit.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * 获取网络时间
     *
     * @param ntpServerHost
     * @return
     * @throws IOException
     */
    public static Date getNetDate(String ntpServerHost) throws IOException {
        return getNetDate(ntpServerHost, DEFAULT_NTP_SERVER_PORT);
    }

    public static Date getNetDate(String ntpServerHost, int port) throws IOException {
        return getNetDate(ntpServerHost, port, 1000);
    }

    public static Date getNetDate(String ntpServerHost, int port, int timeout) throws IOException {
        NTPUDPClient ntpudpClient = new NTPUDPClient();
        try {
            ntpudpClient.open();
            ntpudpClient.setSoTimeout(timeout);
            ntpudpClient.setDefaultTimeout(timeout);
            TimeInfo time = ntpudpClient.getTime(InetAddress.getByName(ntpServerHost), port);
            return new Date(time.getMessage().getTransmitTimeStamp().getTime());
        } finally {
            ntpudpClient.close();
        }
    }

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

    /**
     * 时间是否相同
     * <br><pre>{@code
     * Date d1 = DateFormatUtils.parse("2024-01-01 00:00:11");
     * Date d2 = DateFormatUtils.parse("2024-01-01 00:01:11");
     * boolean sameDay = DateUtils.isSame(d1, d2, TimeUnit.DAYS); // true
     * boolean sameHour = DateUtils.isSame(d1, d2, TimeUnit.HOURS); // true
     * boolean sameMinutes = DateUtils.isSame(d1, d2, TimeUnit.MINUTES); // false
     * boolean sameSeconds = DateUtils.isSame(d1, d2, TimeUnit.SECONDS); // false
     * }</pre>
     *
     * @param d1
     * @param d2
     * @param timeUnit 比较的时间维度
     * @return
     */
    public static boolean isSame(Date d1, Date d2, TimeUnit timeUnit) {
        return timeUnit.convert(d1.getTime(), TimeUnit.MILLISECONDS) == timeUnit.convert(d2.getTime(), TimeUnit.MILLISECONDS);
    }
}
