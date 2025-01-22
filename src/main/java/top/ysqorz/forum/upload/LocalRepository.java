package top.ysqorz.forum.upload;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import top.ysqorz.forum.utils.DateTimeUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 22:58
 */
@Slf4j
@Component
public class LocalRepository implements UploadRepository {

    @Override
    public String[] uploadImage(InputStream inputStream, String filename) throws IOException {
        File staticDir = ResourceUtils.getFile("classpath:static");
        String yearMonth = DateTimeUtils.formatNow(DateTimeUtils.MONTH_WITHOUT_BAR);
        String relativeDir = StrUtil.join(File.separator, "upload", "images", yearMonth);
        File destDir = new File(staticDir, relativeDir);
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + destDir.getAbsolutePath());
            }
        }
        File destFile = new File(destDir, filename);
        upload(inputStream, destFile.getAbsolutePath());
        String original = generateUrl(filename);

        // 制作缩略图
        Thumbnails.of(destFile)
                .size(256, 256)
                .keepAspectRatio(true)
                .toFile(new File(destDir, "thumb_" + filename));
        String thumb = generateUrl("thumb_" + filename);

        return new String[]{original, thumb};
    }

    private String generateUrl(String filePath) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/upload/images/" + filePath)
                .queryParam("timestamp", System.currentTimeMillis())
                .toUriString();

    }

    @Override
    public void upload(InputStream inputStream, String filePath) throws IOException {
        OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(filePath)));
        // 拷贝后自动关闭输入输出流
        FileCopyUtils.copy(inputStream, outputStream);
    }
}
