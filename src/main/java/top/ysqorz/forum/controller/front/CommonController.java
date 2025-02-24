package top.ysqorz.forum.controller.front;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ysqorz.forum.common.annotation.NotWrapWithResultModel;
import top.ysqorz.forum.dto.resp.UploadResult;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.upload.UploadRepository;
import top.ysqorz.forum.upload.uploader.ImageUploader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 包含前后台公用的接口
 * @author passerbyYSQ
 * @create 2021-05-23 23:08
 */
@Validated
@RestController
public class CommonController {

    @Resource
    private RedisService redisService;
    // 为了方便不同组员开发，使用阿里云OSS
    @Resource
    private UploadRepository aliyunOssRepository;
    @Resource
    private RestTemplate restTemplate;

    /**
     * 前后台公用的上传的图片的接口
     */
    @PostMapping("/upload/image")
    public UploadResult uploadImage(@NotNull MultipartFile image) throws IOException  {
        ImageUploader imageUploader = new ImageUploader(image, aliyunOssRepository);
        return imageUploader.upload();
    }

    /**
     * 登录页面的验证码图片(注册页面也用这个接口)
     */
    @GetMapping("/captcha")
    public void captchaImage(@RequestParam String token, HttpServletResponse response) throws IOException {
        // 生成验证码图片
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(120, 46, 4, 16);
        // 将正确验证码保存到Redis
        redisService.saveCaptcha(token, circleCaptcha.getCode());
        // 设置响应头信息，阻止页面缓存；设置响应的MIME类型
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 通过输出流输出验证码图片
        circleCaptcha.write(response.getOutputStream());
    }

    @Value("${zhipu.api-key}")
    private String apiKey;

    // 创建线程池处理异步事件
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    @NotWrapWithResultModel
    @GetMapping(value = "/zhipu", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter testSSE(@NotBlank String content, HttpServletResponse response) {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding("UTF-8");
        // 这行代码设置了Cache-Control HTTP头部字段，值为no-cache。这意味着浏览器不应该缓存此响应。对于SSE来说，这是很重要的，因为我们希望实时更新数据，而不希望浏览器缓存旧的数据。
        response.setHeader("Cache-Control", "no-cache");
        // 这行代码设置了Connection HTTP头部字段，值为keep-alive。这意味着客户端和服务器之间的TCP连接在响应完成后保持打开状态，以便后续的SSE事件可以通过同一个连接发送。这对于持续的数据流非常重要，因为它减少了建立新连接的开销。
        response.setHeader("Connection", "keep-alive");

        ZhipuReqTask task = new ZhipuReqTask(content, response);
        System.out.println("Tomcat线程: " + Thread.currentThread().getName());
        executor.submit(task);
        return task.getEmitter();
    }

    public class ZhipuReqTask implements Runnable {
        public final String DATA_PREFIX = "data:";
        public final String DONE_FLAG = "[DONE]";
        private final String content;
        @Getter
        private final SseEmitter emitter;
        private final HttpServletResponse response;

        public ZhipuReqTask(String content, HttpServletResponse response) {
            this.content = content;
            this.response = response;
            this.emitter = new SseEmitter(0L);  // 0表示不超时
        }

        @Override
        public void run() {
            String body = restTemplate.execute("http://localhost:8080/sse?content=" + content,
                    HttpMethod.GET,
                    null,
                    new ResponseExtractor<String>() {
                        @Override
                        public String extractData(ClientHttpResponse response) throws IOException {
                            StringBuilder sbd = new StringBuilder();
                            try (Reader reader = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8)) {
                                int ch;
                                while ((ch = reader.read()) != -1) {
                                    sbd.append((char) ch);
                                    System.out.print((char) ch);
                                }
                            }
                            return sbd.toString();
                        }
                    });
//            System.out.println("【响应内容】：" + body);
            emitter.complete();
        }
    }

    @NotWrapWithResultModel
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents(HttpServletResponse response) {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setCharacterEncoding("UTF-8");
        // 这行代码设置了Cache-Control HTTP头部字段，值为no-cache。这意味着浏览器不应该缓存此响应。对于SSE来说，这是很重要的，因为我们希望实时更新数据，而不希望浏览器缓存旧的数据。
        response.setHeader("Cache-Control", "no-cache");
        // 这行代码设置了Connection HTTP头部字段，值为keep-alive。这意味着客户端和服务器之间的TCP连接在响应完成后保持打开状态，以便后续的SSE事件可以通过同一个连接发送。这对于持续的数据流非常重要，因为它减少了建立新连接的开销。
        response.setHeader("Connection", "keep-alive");
//        response.setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked");

        SseEmitter emitter = new SseEmitter(0L); // 0表示不超时

        new Thread(() -> {
            try {
                for (int i = 0; i < 5; i++) {
                    // 模拟数据生成
                    String data = "Event " + i + " at " + System.currentTimeMillis();

                    // 发送事件
                    emitter.send(
                            SseEmitter.event()
//                                    .id(String.valueOf(i))        // 事件ID
//                                    .name("message")              // 事件名称
                                    .data(data)                   // 事件数据
//                                    .reconnectTime(5000)          // 重连时间
                    );

                    // 间隔1秒
                    Thread.sleep(1000);
                }

                // 完成发送
               emitter.complete();
            } catch (IOException | InterruptedException e) {
                // 发生错误时终止连接
                emitter.completeWithError(e);
            }
        }).start();

        // 处理连接关闭
//        emitter.onCompletion(() -> System.out.println("SSE connection completed"));
//        emitter.onTimeout(() -> System.out.println("SSE connection timed out"));
//        emitter.onError((ex) -> System.out.println("SSE error: " + ex.getMessage()));


        return emitter;
    }
}
