package top.ysqorz.forum.upload;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.utils.DateTimeUtils;

import java.io.InputStream;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 1:22
 */
@Component
@ConfigurationProperties(prefix = "upload.repository.aliyun-oss")
public class AliyunOssRepository extends OssUploadRepository {

    @Override
    public String[] uploadImage(InputStream inputStream, String filename) {
        String yearMonth = DateTimeUtils.formatNow(DateTimeUtils.MONTH_WITHOUT_BAR);
        // oss只认斜杠，与操作系统的文件路径分隔符无关
        String objectName = StrUtil.join("/", "images", yearMonth, filename);
        upload(inputStream, objectName);

        // 拼接url。仅限于权限为公共读
        String original = domainName + "/" + objectName; // 原图
        String thumb = original + "?x-oss-process=style/face_img"; // 缩略图

        return new String[]{original, thumb};
    }

    @Override
    public void upload(InputStream inputStream, String filePath) {
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, filePath, inputStream);
        ossClient.shutdown();
    }
}
