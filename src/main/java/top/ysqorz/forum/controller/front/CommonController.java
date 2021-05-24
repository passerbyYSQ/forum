package top.ysqorz.forum.controller.front;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.ysqorz.forum.common.FileUploadException;
import top.ysqorz.forum.upload.ImageUploader;
import top.ysqorz.forum.upload.UploadRepository;
import top.ysqorz.forum.vo.ResultModel;
import top.ysqorz.forum.vo.UploadResult;

import javax.annotation.Resource;
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

    // 为了方便不同组员开发，使用阿里云OSS
    @Resource
    private UploadRepository aliyunOssRepository;

    /**
     * 前后台公用的上传的图片的接口
     */
    @PostMapping("/upload/image")
    public ResultModel<UploadResult> uploadImage(@NotNull MultipartFile image)
            throws IOException, FileUploadException {
        ImageUploader imageUploader = new ImageUploader(image, aliyunOssRepository);
        UploadResult uploadResult = imageUploader.upload();
        return ResultModel.success(uploadResult);
    }

}
