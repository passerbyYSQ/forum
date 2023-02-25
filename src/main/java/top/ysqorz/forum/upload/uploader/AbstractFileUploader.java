package top.ysqorz.forum.upload.uploader;

import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ysqorz.forum.common.exception.ParamInvalidException;
import top.ysqorz.forum.dto.resp.UploadResult;
import top.ysqorz.forum.upload.UploadRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author passerbyYSQ
 * @create 2021-05-17 23:32
 */
public abstract class AbstractFileUploader implements FileUploader {

    protected UploadRepository repository;
    private MultipartFile multipartFile;
    // 文件后缀
    private String suffix;
    // 文件大小
    private DataSize fileSize;

    // 允许上传的最大文件大小
    private DataSize allowedMaximumFileSize;
    // 允许的媒体类型
    private Set<MediaType> allowedMediaTypes = new HashSet<>();
    // 允许的后缀格式（不需要点。例如：jpg，png，zip）
    private Set<String> allowedSuffix = new HashSet<>();


    public AbstractFileUploader(MultipartFile multipartFile, UploadRepository repository,
                                DataSize allowedMaximumFileSize, String... suffix) {
        this.multipartFile = multipartFile;
        this.repository = repository;
        this.allowedMaximumFileSize = allowedMaximumFileSize;
        addAllowedSuffix(suffix);
    }

    /**
     * 上传
     */
    @Override
    public UploadResult upload() throws IOException {
        // 先完成校验
        if (multipartFile.getSize() <= 0) {
            throw new ParamInvalidException("上传文件不能为空");
        }
        if (!checkSuffix(multipartFile.getOriginalFilename())) {
            throw new ParamInvalidException("不支持该文件格式：" + this.suffix);
        }
        if (!checkContentType(multipartFile.getContentType())) {
            throw new ParamInvalidException("不支持该媒体类型：" + multipartFile.getContentType());
        }

        if (!checkFileSize(multipartFile.getSize())) {
            throw new ParamInvalidException(
                    String.format("当前文件大小：%d MB，超出可上传的最大值：%d MB",
                            fileSize.toMegabytes(), allowedMaximumFileSize.toMegabytes()));
        }


        String newFilename = generateNewFilename();

        String[] url = doUpload(multipartFile.getInputStream(), newFilename); // 上传

        return new UploadResult(newFilename, fileSize.toBytes(), suffix, url);
    }

    protected abstract String[] doUpload(InputStream inputStream, String newFilename)
            throws IOException;

    @Override
    public boolean checkFileSize(long bytes) {
        if (!ObjectUtils.isEmpty(allowedMediaTypes)) {
            this.fileSize = DataSize.ofBytes(bytes);
            return bytes <= allowedMaximumFileSize.toBytes();
        }
        return true;
    }

    @Override
    public boolean checkContentType(String contentType) {
        if (!StringUtils.isEmpty(contentType) && !ObjectUtils.isEmpty(allowedMediaTypes)) {
            MediaType mediaType = MediaType.valueOf(contentType);
            for (MediaType allowedMediaType : allowedMediaTypes) {
                if (allowedMediaType.includes(mediaType)) { // 只要有一个符包含，就返回true
                    return true;
                }
            }
            return false; // 没有一个包含，返回false
        }
        return true; // 没有设置allowedMediaTypes，则默认true
    }

    @Override
    public boolean checkSuffix(String originalFilename) {
        if (!StringUtils.isEmpty(originalFilename)) {
            int index = originalFilename.lastIndexOf(".");
            this.suffix = originalFilename.substring(index + 1).toLowerCase(); // 小写
            if (!ObjectUtils.isEmpty(allowedSuffix)) {
                return allowedSuffix.contains(this.suffix);
            }
        }
        return true; // 没有设置allowedSuffix，则默认true
    }

    /**
     * 生成新文件名的方法，此处提供默认实现。子类可以根据自己情况复写
     */
    @Override
    public String generateNewFilename() {
        return UUID.randomUUID().toString().replace("-","")
                + "." + this.suffix; // 拼接后缀名
    }

    /**
     * 添加支持的媒体类型
     */
    public void addAllowedMediaType(String... mediaTypes) {
        for (String contentType : mediaTypes) {
            allowedMediaTypes.add(MediaType.valueOf(contentType));
        }
    }

    public void setRepository(UploadRepository repository) {
        this.repository = repository;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public void setAllowedMaximumFileSize(DataSize allowedMaximumFileSize) {
        this.allowedMaximumFileSize = allowedMaximumFileSize;
    }

    public void addAllowedSuffix(String... suffix) {
        for (String s : suffix) {
            allowedSuffix.add(s.toLowerCase()); // 小写
        }
    }
}
