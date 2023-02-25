package top.ysqorz.forum.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class SerializationConfig {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String dateTimePattern;

    /**
     * 定制ObjectMapper，使之能序列化LocalDateTime对象
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        LocalDateTimeSerializer dateTimeSerializer = new LocalDateTimeSerializer(dateTimeFormatter);
        LocalDateTimeDeserializer dateTimeDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);
        return builder.serializationInclusion(JsonInclude.Include.NON_NULL) // 不序列化空的字段
                .serializerByType(LocalDateTime.class, dateTimeSerializer)
                .deserializerByType(LocalDateTime.class, dateTimeDeserializer)
                .createXmlMapper(false)
                .build();
    }

    @Bean
    public MappingJackson2JsonView mappingJackson2JsonView(ObjectMapper objectMapper) {
        return new MappingJackson2JsonView(objectMapper);
    }
}
