package top.ysqorz.forum.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * @author passerbyYSQ
 * @create 2023-06-23 23:28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiAccessLimit {
    int duration() default 60;

    ChronoUnit unit() default ChronoUnit.SECONDS;

    int maxCount() default 60;

    /**
     * 封禁时长
     */
    int blackDuration() default 24;

    ChronoUnit blackUnit() default ChronoUnit.HOURS;
}
