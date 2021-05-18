package top.ysqorz.forum.upload;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author passerbyYSQ
 * @create 2021-05-18 22:58
 */
@Component
public class LocalRepository implements UploadRepository {

    @Override
    public String[] uploadImage(InputStream inputStream, String filename) {
        try {
            File staticDir = ResourceUtils.getFile("classpath:static");
            File destDir = new File(staticDir, "upload/images");
            if (!destDir.exists()) {
                destDir.mkdirs(); // 递归创建创建多级
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

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateUrl(String filePath) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/upload/images/" + filePath)
                .queryParam("timestamp", System.currentTimeMillis())
                .toUriString();

    }

    @Override
    public void upload(InputStream inputStream, String filePath) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filePath);
            FileCopyUtils.copy(inputStream, outStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
