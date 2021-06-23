package top.ysqorz.forum.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * Java8新时间API的工具类。项目用到的再添加相关的封装操作
 *
 * @author passerbyYSQ
 * @create 2021-04-21 15:29
 */
public class DateTimeUtils {

    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    // Date => LocalDateTime
    public static LocalDateTime toLocalDateTime(Date date) {
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    // LocalDateTime => Date
    public static Date toDate(LocalDateTime dateTime) {
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    // 相差多少个时间单位
    public static long dif(Temporal early, Temporal late, ChronoUnit unit) {
        return unit.between(early, late);
    }




}
