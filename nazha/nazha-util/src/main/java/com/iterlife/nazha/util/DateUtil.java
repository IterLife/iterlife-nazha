package com.iterlife.nazha.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * @desc：TODO
 * @date：2019-05-20 14:11
 * @author：lujie
 */
public class DateUtil {
    private static DateTimeFormatter defaultDateFmt = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static DateTimeFormatter defaultTimeFmt = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
    private static ZoneId defaultZoneId = ZoneId.systemDefault();

    /**
     * 日期字符串转换为 LocalDate，默认格式 yyyyMMdd
     *
     * @param dateStr 日期字符串
     */
    public static LocalDate toLocalDate(String dateStr) {
        return toLocalDate(dateStr, defaultDateFmt);
    }


    /**
     * 时间字符串转换为 LocalTime，默认格式 HH:mm:ss:SSS
     *
     * @param timeStr 时间字符串
     */
    public static LocalTime toLocalTime(String timeStr) {
        return toLocalTime(timeStr, defaultTimeFmt);
    }

    /**
     * 日期字符串转换为 LocalDate，指定格式 fmt
     *
     * @param dateStr 日期字符串
     * @param fmt     日期字符串格式
     */
    public static LocalDate toLocalDate(String dateStr, DateTimeFormatter fmt) {
        return LocalDate.parse(dateStr, fmt);
    }

    /**
     * 时间字符串转换为 LocalTime，指定格式 fmt
     *
     * @param timeStr 时间字符串
     * @param fmt     时间字符串格式
     */
    public static LocalTime toLocalTime(String timeStr, DateTimeFormatter fmt) {
        return LocalTime.parse(timeStr, fmt);
    }

    /**
     * 日切格式互相转换
     */
    public static Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(defaultZoneId).toInstant());
    }

    /**
     * 日切格式互相转换
     */
    public static LocalDateTime toLocalDateTime(Date dateTime) {
        return dateTime.toInstant().atZone(defaultZoneId).toLocalDateTime();
    }

    /**
     * 日期字符串转换为 LocalDate，指定格式 fmt
     *
     * @param date 日期
     * @return 日期字符串, 默认 yyyyMMdd 格式
     */
    public static String toString(LocalDate date) {
        return date.format(defaultDateFmt);
    }
}
