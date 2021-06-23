package top.ysqorz.forum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author passerbyYSQ
 * @create 2021-01-29 14:41
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 前后端分离需要解决跨域问题
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // 放行哪些原始域
                .allowCredentials(true) // 是否发送cookie
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .exposedHeaders("*")
                .allowedHeaders("*") // allowedHeaders是exposedHeaders的子集
                .maxAge(3600); // 预检请求OPTIONS请求的缓存时间
    }

    /**
     * 在参数绑定时，自定义String->String的转换器，
     * 在转换逻辑中对参数值进行转义，从而达到防XSS的效果
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EscapeStringConverter());
        //registry.addFormatter(new LocalDateTimeFormatter());

    }
}
