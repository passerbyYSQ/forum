package top.ysqorz.forum.controller.front;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.json.JSONObject;
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

    private static final String API_KEY = "xxx";

    @GetMapping("/sse")
    public void testSSE(@NotBlank String content) {
        List<ZhipuChatMessage> messages = new ArrayList<>();
        messages.add(new ZhipuChatMessage("user", content));
        ZhiChatReq req = ZhiChatReq.builder()
                .model("GLM-4-Flash")
                .stream(true)
                .messages(messages)
                .build();
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
//        header.set(HttpHeaders.ACCEPT, MediaType.TEXT_EVENT_STREAM_VALUE);
        header.set(HttpHeaders.AUTHORIZATION, "Bearer " + API_KEY);
        header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(new HttpEntity<>(req, header));
        restTemplate.execute("https://open.bigmodel.cn/api/paas/v4/chat/completions",
                HttpMethod.POST,
                requestCallback,
                new SSEResponseExtractor<>(new SSEResponseCallback<JSONObject>() {
                    @Override
                    public void onLineRead(String str, JSONObject data) {
//                        System.out.println(str);
                    }

                    @Override
                    public void onCompletedRead(String str, List<JSONObject> dataList) {

                    }

                    @Override
                    public JSONObject convertLine(String str) {
//                        return JsonUtils.jsonToObj(str, JSONObject.class);
                        return null;
                    }
                }));
    }
}
