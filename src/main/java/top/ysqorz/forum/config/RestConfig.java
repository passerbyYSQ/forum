package top.ysqorz.forum.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-10-16 15:45
 */
@Configuration
public class RestConfig {

    @Bean
    public HttpClient httpClient() throws Exception {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 设置信任ssl访问
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
        httpClientBuilder.setSSLContext(sslContext);
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                // 注册http和https请求
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory).build();
        // 使用Httpclient连接池的方式配置(推荐)，同时支持netty，okHttp以及其他http框架
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        // 最大连接数
        connectionManager.setMaxTotal(1000);
        // 同路由并发数
        connectionManager.setDefaultMaxPerRoute(100);
        // 配置连接池
        httpClientBuilder.setConnectionManager(connectionManager);
        // 重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, true));
        // 设置默认请求头
        List<Header> headers = new ArrayList<>();
        // ...
        httpClientBuilder.setDefaultHeaders(headers);
        return httpClientBuilder.build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // 连接超时(毫秒)，这里设置10秒
        requestFactory.setConnectTimeout(10 * 1000);
        // 数据读取超时时间(毫秒)，这里设置60秒
        requestFactory.setReadTimeout(60 * 1000);
        // 从连接池获取请求连接的超时时间(毫秒)，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
        requestFactory.setConnectionRequestTimeout(10 * 1000);
        return requestFactory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory requestFactory, RestTemplateLogger restLogger, ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate();
        // https://www.jianshu.com/p/96c1bef7856b
        // 通过BufferingClientHttpRequestFactory对象包装现有的ResquestFactory，用来支持多次调用getBody()方法（增加了日志打印所引起）
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(requestFactory));
        restTemplate.setInterceptors(Collections.singletonList(restLogger));
        // 使用被我们定制过的的JSON转换器
        restTemplate.getMessageConverters().removeIf(messageConverter -> messageConverter instanceof MappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().add(0, mappingJackson2HttpMessageConverter(objectMapper));
        return restTemplate;
    }

    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        // 当响应的Content-Type为text/html，但是内容是JSON串时无法反序列化
        List<MediaType> mediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.TEXT_PLAIN);
        converter.setSupportedMediaTypes(mediaTypes);
        return converter;
    }
}
