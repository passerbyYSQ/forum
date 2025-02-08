package top.ysqorz.forum.controller.front;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import lombok.Getter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import top.ysqorz.forum.common.annotation.NotWrapWithResultModel;
import top.ysqorz.forum.controller.ai.SSEResponseCallback;
import top.ysqorz.forum.controller.ai.SSEResponseExtractor;
import top.ysqorz.forum.controller.ai.ZhiChatReq;
import top.ysqorz.forum.controller.ai.ZhipuChatMessage;
import top.ysqorz.forum.dto.resp.UploadResult;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.upload.UploadRepository;
import top.ysqorz.forum.upload.uploader.ImageUploader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public SseEmitter testSSE(@NotBlank String content) {
        ZhipuReqTask task = new ZhipuReqTask(restTemplate, apiKey, content);
        executor.submit(task);
        return task.getEmitter();
    }

    public static class ZhipuReqTask implements Runnable {
        public final String DATA_PREFIX = "data:";
        public final String DONE_FLAG = "[DONE]";
        private final RestTemplate restTemplate;
        private final String apiKey;
        private final String content;
        @Getter
        private final SseEmitter emitter;

        public ZhipuReqTask(RestTemplate restTemplate, String apiKey, String content) {
            this.restTemplate = restTemplate;
            this.apiKey = apiKey;
            this.content = content;
            this.emitter = new SseEmitter(0L);  // 0表示不超时
        }

        @Override
        public void run() {
            List<ZhipuChatMessage> messages = new ArrayList<>();
            messages.add(new ZhipuChatMessage("user", content));
            ZhiChatReq req = ZhiChatReq.builder()
                    .model("GLM-4-Flash")
                    .stream(true)
                    .messages(messages)
                    .build();
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.set(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE);
            header.set(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey);
            header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(req, header));
            //{"id":"20250206093013a182c11a9afe42c8","created":1738805413,"model":"glm-4-flash","choices":[{"index":0,"delta":{"role":"assistant","content":"。"}}]}
            //{"id":"20250206093013a182c11a9afe42c8","created":1738805413,"model":"glm-4-flash","choices":[{"index":0,"finish_reason":"stop","delta":{"role":"assistant","content":""}}],"usage":{"prompt_tokens":7,"completion_tokens":46,"total_tokens":53}}

            SSEResponseExtractor<String> respExtractor = new SSEResponseExtractor<>(DATA_PREFIX, DONE_FLAG, new SSEResponseCallback<String>() {
                @Override
                public void onLineRead(String str, String data) throws IOException {
                    emitter.send(SseEmitter.event().data(data));
                }

                @Override
                public void onCompletedRead(String str, List<String> dataList) {
                    emitter.complete();
                }

                @Override
                public String convertLine(String str) {
                    return str;
                }
            });

            restTemplate.execute("https://open.bigmodel.cn/api/paas/v4/chat/completions",
                    HttpMethod.POST,
                    requestCallback,
                    respExtractor);
        }
    }

    @NotWrapWithResultModel
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        SseEmitter emitter = new SseEmitter(0L); // 0表示不超时

        // 获取当前 Shiro 上下文的 Subject
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        System.out.println("主线程：" + Thread.currentThread().getName());

        new Thread(() -> {
            try {
                // 绑定 SecurityManager 到当前线程，以便 Shiro 访问
                ThreadContext.bind(securityManager);
                System.out.println("新创建的线程 bind：" + Thread.currentThread().getName());

                for (int i = 0; i < 3; i++) {
                    // 模拟数据生成
                    String data = "Event " + i + " at " + System.currentTimeMillis();

                    // 发送事件
                    emitter.send(
                            SseEmitter.event()
                                    .id(String.valueOf(i))        // 事件ID
                                    .name("message")              // 事件名称
                                    .data(data)                   // 事件数据
                                    .reconnectTime(5000)          // 重连时间
                    );

                    // 间隔1秒
                    Thread.sleep(1000);
                }

                // 完成发送
               emitter.complete();
            } catch (IOException | InterruptedException e) {
                // 发生错误时终止连接
                emitter.completeWithError(e);
            } finally {
                // 解绑 SecurityManager
                ThreadContext.unbindSecurityManager();
                System.out.println("新创建的线程 unBind：" + Thread.currentThread().getName());
            }
        }).start();

        // 处理连接关闭
//        emitter.onCompletion(() -> System.out.println("SSE connection completed"));
//        emitter.onTimeout(() -> System.out.println("SSE connection timed out"));
//        emitter.onError((ex) -> System.out.println("SSE error: " + ex.getMessage()));

        return emitter;
    }
}
