package top.ysqorz.forum.config.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author passerbyYSQ
 * @create 2023-06-24 23:18
 */
@Component
public class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateTimePattern;

    @Override
    public LocalDateTime convert(String source) {
        return ObjectUtils.isEmpty(source) ? null :
                LocalDateTime.parse(source, DateTimeFormatter.ofPattern(dateTimePattern));
    }
}
