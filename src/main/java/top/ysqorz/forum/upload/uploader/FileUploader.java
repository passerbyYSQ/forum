package top.ysqorz.forum.upload.uploader;

import top.ysqorz.forum.dto.resp.UploadResult;

import java.io.IOException;

/**
 * @author passerbyYSQ
 * @create 2021-05-17 23:25
 */
public interface FileUploader {

    /**
     * 检查文件允许上传的最大文件大小
     */
    boolean checkFileSize(long bytes);

    /**
     * 检查文件类型是否是允许的类型
     */
    boolean checkContentType(String contentType);

    /**
     * 检查文件后缀名
     */
    boolean checkSuffix(String fileName);

    /**
     * 生成新的文件名
     */
    String generateNewFilename();

    /**
     * 上传
     */
    UploadResult upload() throws IOException;


}
