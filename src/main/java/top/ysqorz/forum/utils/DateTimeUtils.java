package top.ysqorz.forum.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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

    // 获取当天格式化后的日期。例：2021-07-02
    public static String getFormattedDate() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
    }

    // 距离下一天凌晨，还有多长时间（可精确到纳秒）
    public static Duration durationToNextDay() {
        // 次日凌晨的时间，LocalDateTime.of(日期, 时间)
        LocalDateTime nextDay = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIN);
        // 当前距离次日凌晨的时长
        return Duration.between(LocalDateTime.now(), nextDay); // !!!
    }

    // 获取到当周格式化后的年数和周数，以周一为一周的开始。例：2021-07-11 => 2021-29
    public static String getFormattedWeek() {
        DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter weekFormatter = DateTimeFormatter.ofPattern("w");
        String year = LocalDateTime.now().format(yearFormatter);
        Integer week = Integer.valueOf(LocalDateTime.now().format(weekFormatter));
        // LocalDateTime默认周日为一周第一天，而我们需要周一为第一天，所以在周日时week--
        if (LocalDateTime.now().getDayOfWeek().getValue() == 7) {
            week--;
        }
        return week < 10 ? year + "-0" + week.toString() : year + "-" + week.toString();
    }

    // 距离下周一凌晨，还有多少时间（可精确到纳秒）
    public static Duration durationToNextWeek() {
        // 下周一的日期
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        // 下周一凌晨的时间，LocalDateTime.of(日期, 时间)
        LocalDateTime nextWeek = LocalDateTime.of(nextMonday, LocalTime.MIN);
        return Duration.between(LocalDateTime.now(), nextWeek);
    }

}
