package top.ysqorz.forum.controller.front;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ysqorz.forum.dto.resp.UploadResult;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.upload.UploadRepository;
import top.ysqorz.forum.upload.uploader.ImageUploader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

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

}
