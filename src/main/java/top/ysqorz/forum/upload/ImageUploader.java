package top.ysqorz.forum.upload;

import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.common.FileUploadException;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.vo.UploadResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 0:10
 */
public class ImageUploader extends AbstractFileUploader {

    public static final Pattern BASE64_PATTERN = Pattern.compile(Constant.BASE64_REGEX);
    private String base64;

    private ImageUploader() {
        super(null, null, DataSize.ofMegabytes(10),
                "jpg", "jpeg", "png", "gif", "svg", "bmp", "tif");
        addAllowedMediaType("image/*");
    }

    public ImageUploader(MultipartFile multipartFile, UploadRepository repository) {
        this();
        setMultipartFile(multipartFile);
        setRepository(repository);
    }

    public ImageUploader(String base64, UploadRepository repository) {
        this();
        setRepository(repository);
        this.base64 = base64;
    }

    public UploadResult uploadBase64() throws FileUploadException, ParameterErrorException, IOException {
        Matcher matcher = BASE64_PATTERN.matcher(this.base64);
        if (!matcher.find()) {
            throw new ParameterErrorException("base64编码错误");
        }
        String suffix = matcher.group(2); // 取出图片的后缀
        if (!checkSuffix("." + suffix)) {
            throw new FileUploadException("不支持该图片类型");
        }
        String data = matcher.group(3); // 取出data部分
        byte[] bytes = Base64.getDecoder().decode(data);
        if (!checkFileSize(bytes.length)) {
            throw new FileUploadException("不支持该图片类型");
        }

        String newFilename = generateNewFilename();
        String[] url = doUpload(new ByteArrayInputStream(bytes), newFilename);
        return new UploadResult(newFilename, (long) bytes.length, suffix, url);
    }

    @Override
    protected String[] doUpload(InputStream inputStream, String newFilename) throws IOException {
        return repository.uploadImage(inputStream, newFilename);
    }
}
