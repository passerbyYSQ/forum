package top.ysqorz.forum.admin.controller;

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
 * @author passerbyYSQ
 * @create 2021-05-18 23:49
 */
@RestController
@Validated
public class TestUploadController {

    @Resource
    private UploadRepository localRepository;

    @Resource
    private UploadRepository aliyunOssRepository;

    @PostMapping("/uploadImage")
    public ResultModel<UploadResult> uploadImage(@NotNull MultipartFile file) // 不能为空！！！
            throws IOException, FileUploadException {
        ImageUploader imageUploader = new ImageUploader(file, aliyunOssRepository);
        UploadResult uploadResult = imageUploader.upload();
        return ResultModel.success(uploadResult);
    }

}
