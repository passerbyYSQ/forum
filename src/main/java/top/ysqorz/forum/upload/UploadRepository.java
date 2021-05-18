package top.ysqorz.forum.upload;

import java.io.InputStream;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 1:22
 */
public interface UploadRepository {

    /**
     * @param inputStream
     * @param filename      文件名。包含后缀
     * @return
     */
    String[] uploadImage(InputStream inputStream, String filename);

    /**
     * @param inputStream
     * @param filePath      文件的完整路径
     */
    void upload(InputStream inputStream, String filePath);

}
