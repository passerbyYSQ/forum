package top.ysqorz.forum.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2021-01-31 23:47
 */
public class QRCodeUtils {

    public static void main(String[] args) throws IOException, WriterException {
        BufferedImage image = QRCodeUtils.generate(320, "https://blog.csdn.net/qq_43290318");
        BufferedImage imageWithLogo = QRCodeUtils.generateWithLogo(image, "https://avatar.csdnimg.cn/1/2/C/1_qq_43290318_1569377523.jpg");
    }

    /**
     * 生成不带logo的二维码
     * @param width     320
     * @param content   字符串越长，生成的二维码看起来越密
     * @return          返回不带logo的BufferedImage
     */
    public static BufferedImage generate(int width, String content) throws WriterException {
        int height = width;
        // 相关参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //内容编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错等级为高，因为要设置logo
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);

        // 不带logo的二维码图片
        BitMatrix bitMatrix = new MultiFormatWriter()
                .encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        // 输出测试
//        MatrixToImageWriter.writeToPath(bitMatrix, "jpg", new File("D:\\zxing.jpg").toPath());// 输出原图片
//        System.out.println("输出成功：D:\\zxing.jpg");

        MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(
                0xFF000001, 0xFFFFFFFF);
        /*
            问题：生成二维码正常,生成带logo的二维码logo变成黑白
            原因：MatrixToImageConfig默认黑白，需要设置BLACK、WHITE
            解决：https://ququjioulai.iteye.com/blog/2254382
         */
        return MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);
    }

    /**
     * 生成带有logo的二维码。
     * 异常统一抛出，在业务层再统一捕获
     * @param matrixImage   源二维码图片
     * @param logoUrl       用户头像的公网url
     * @return              返回带有logo的BufferedImage
     * 参考：https://blog.csdn.net/weixin_39494923/article/details/79058799
     */
    public static BufferedImage generateWithLogo(BufferedImage matrixImage, String logoUrl) throws IOException {
        // 读取二维码图片，并构建绘图对象
        Graphics2D g2 = matrixImage.createGraphics();
        // 消除锯齿。不设置的话，圆弧效果会出现锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int matrixWidth = matrixImage.getWidth();
        int matrixHeight = matrixImage.getHeight();

        // 读取logo图片
        BufferedImage logo = ImageIO.read(new URL(logoUrl));

        // 在底图（源二维码）上绘制logo。logo大小为地图的2/5
//        g2.drawImage(logo, matrixWidth / 5 * 2, matrixHeight / 5 * 2,
//                matrixWidth / 5, matrixHeight / 5, null);//绘制
        // 采用平滑缩放的方式，来解决logo质量变差的问题
        g2.drawImage(logo.getScaledInstance(matrixWidth / 5,matrixWidth / 5, Image.SCALE_SMOOTH),
                matrixWidth / 5 * 2, matrixHeight / 5 * 2, null);

        // 圆滑的粗线条
        // 参见：https://blog.csdn.net/li_tengfei/article/details/6098093
        BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        // 设置画笔的画出的线条
        g2.setStroke(stroke);

        // 指定弧度的圆角矩形（空心的，白色边的）
        RoundRectangle2D.Double round = new RoundRectangle2D.Double(matrixWidth / 5 * 2, matrixHeight / 5 * 2,
                matrixWidth / 5, matrixHeight / 5, 5, 5);
        g2.setColor(Color.white);
        // 用于绘制logo外层的白边
        g2.draw(round);

        // 圆滑的细线条
        BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g2.setStroke(stroke2);
        // 设置圆角矩形
        RoundRectangle2D.Double round2 = new RoundRectangle2D.Double(matrixWidth / 5 * 2 + 2, matrixHeight / 5 * 2 + 2,
                matrixWidth / 5 - 4, matrixHeight / 5 - 4, 5, 5);
        g2.setColor(new Color(211, 211, 211));
        // 绘制logo内部的灰色边框
        g2.draw(round2);

        g2.dispose();
        matrixImage.flush();

        // 输出测试
//        ImageIO.write(matrixImage, "jpg", new File("D:\\zxing1.jpg"));
//        System.out.println("输出成功：D:\\zxing1.jpg");

        return matrixImage;
    }


}
