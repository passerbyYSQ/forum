package top.ysqorz.forum.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * Java8新时间API的工具类。项目用到的再添加相关的封装操作
 *
 * @author passerbyYSQ
 * @create 2021-04-21 15:29
 */
public class DateTimeUtils {
    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter MONTH = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter MONTH_WITHOUT_BAR = DateTimeFormatter.ofPattern("yyyyMM");

    // 获取当天格式化后的日期。例：2021-07-02
    public static String getFormattedDate() {
        return LocalDate.now().format(DATE);
    }

    // 获取当天格式化后的日期(精确到秒)。例：2021-07-04 20:17:20
    public static String getFormattedTime() {
        return LocalDateTime.now().format(TIME);
    }

    public static String formatNow(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    // LocalDateTime => Instant
    public static Instant toInstant(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    // Date => LocalDateTime
    public static LocalDateTime toLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
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
        return week < 10 ? year + "-0" + week : year + "-" + week;
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
