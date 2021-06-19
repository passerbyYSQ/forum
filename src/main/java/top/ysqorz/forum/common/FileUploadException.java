package top.ysqorz.forum.common;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 0:14
 */
public class FileUploadException extends RuntimeException {
    public FileUploadException() {
        super();
    }

    public FileUploadException(String message) {
        super(message);
    }
}
