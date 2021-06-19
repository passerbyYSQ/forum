package top.ysqorz.forum.controller.front;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.dto.UploadResult;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.upload.UploadRepository;
import top.ysqorz.forum.upload.uploader.ImageUploader;
import top.ysqorz.forum.utils.CaptchaUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
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
    public ResultModel<UploadResult> uploadImage(@NotNull MultipartFile image)
            throws IOException  {
        ImageUploader imageUploader = new ImageUploader(image, aliyunOssRepository);
        UploadResult uploadResult = imageUploader.upload();
        return ResultModel.success(uploadResult);
    }

    /**
     * 登录页面的验证码图片(注册页面也用这个接口)
     */
    @GetMapping("/captcha")
    public void captchaImage(@RequestParam String token, HttpServletResponse response)
            throws ServletException, IOException {
        String captcha = CaptchaUtils.generateAndOutput(response);
        redisService.saveCaptcha(token, captcha);
    }

}
