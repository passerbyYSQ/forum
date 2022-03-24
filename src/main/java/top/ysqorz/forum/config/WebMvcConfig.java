package top.ysqorz.forum.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.ysqorz.forum.config.interceptor.ApiAccessLimitInterceptor;
import top.ysqorz.forum.config.interceptor.HeaderInfoInterceptor;

import javax.annotation.Resource;

/**
 * @author passerbyYSQ
 * @create 2021-01-29 14:41
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private ApiAccessLimitInterceptor apiAccessLimitInterceptor;
    @Resource
    private HeaderInfoInterceptor headerInfoInterceptor;
    @Resource
    private EscapeStringConverter escapeStringConverter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiAccessLimitInterceptor) // 不要用new ApiAccessLimitInterceptor()
                .addPathPatterns("/**")
                .excludePathPatterns("/error", "/im/push"); // 注意/im/push是IM服务转发消息调用的，不是客户端调的，不能统计
        registry.addInterceptor(headerInfoInterceptor)
                .addPathPatterns("/video/*", "/post/detail/*", "/", "/index", "/post/publish",
                        "/user/login", "/user/reg", "/user/home/*", "/user/center/set", "/user/center/home",
                        "/user/center/message", "/user/chat");
    }

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
        registry.addConverter(escapeStringConverter);

    }
}
