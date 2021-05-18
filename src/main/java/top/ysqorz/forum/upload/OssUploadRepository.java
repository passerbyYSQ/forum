package top.ysqorz.forum.upload;

import lombok.Getter;
import lombok.Setter;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 1:34
 */
@Getter
@Setter
public abstract class OssUploadRepository implements UploadRepository {

    protected String accessKeyId;
    protected String accessKeySecret;
    protected String bucketName;
    protected String domainName;
    protected String endPoint;

}
