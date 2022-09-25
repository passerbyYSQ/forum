package top.ysqorz.forum.utils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * 生成简单的验证码图片的工具类
 * 里面的数值最好不要改动，目前的数值生成的验证码图片还算不错
 *
 * @author	passerbyYSQ
 * @date	2020-9-30 20:24:02
 */
public class CaptchaUtils {

	private static Integer width = 120;
	private static Integer height = 46;
	private static Integer charCnt = 4; // 验证码图片中字符的个数
	private static Integer lineCnt = 8;

	private static Random r = new SecureRandom(); // 用于生产随机数

	public static String generateAndOutput(HttpServletResponse response)
			throws ServletException, IOException {
		// 设置响应头信息，阻止页面缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 设置响应的MIME类型
		response.setContentType("image/jpeg");

		// 创建图片对象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 创建画笔
		Graphics g = image.getGraphics();

		// 填充背景色
		g.setColor(getBgColor());
		g.fillRect(0, 0, width, height);

		// 设置画笔
		g.setFont(new Font("黑体", Font.BOLD, 30));

		// 写字符
		StringBuilder sbd = new StringBuilder();
		for (int i = 1; i <= charCnt; i++) {
			g.setColor(getCharColor());
			int x = width / (charCnt + 2) * i;
			int y = height * 4 / 5;
			char c = getRandomChar();
			sbd.append(c);
			g.drawString(c + "", x, y);
		}

		// 画线
		for (int i = 1; i < lineCnt; i++) {
			int x1 = r.nextInt(width);
			int y1 = r.nextInt(height);

			int x2 = r.nextInt(width);
			int y2 = r.nextInt(height);

			g.setColor(getLineColor());
			g.drawLine(x1, y1, x2, y2);
		}

		// 向网络流输出验证码图片
		ImageIO.write(image, "jpeg", response.getOutputStream());

		// 返回验证码字符串
		return sbd.toString();
	}

	/**
	 * 生成一个在区间[low, high]的随机整数
	 *
	 * @param low
	 * @param high
	 * @return
	 */
	private static int getInt(int low, int high) {
		return (low + r.nextInt(high - low + 1));
	}

	/**
	 * 生成一种背景色（相对字符颜色来说较浅）
	 *
	 * @return
	 */
	private static Color getBgColor() {
		int red = getInt(170, 255);
		int green = getInt(170, 255);
		int blue = getInt(170, 255);
		return new Color(red, green, blue);
	}

	/**
	 * 生成一种干扰线的颜色
	 *
	 * @return
	 */
	private static Color getLineColor() {
		int red = getInt(85, 169);
		int green = getInt(85, 169);
		int blue = getInt(85, 169);
		return new Color(red, green, blue);
	}

	/**
	 * 生成一种字符颜色（相对字体颜色较深）
	 *
	 * @return
	 */
	private static Color getCharColor() {
		int red = getInt(0, 84);
		int green = getInt(0, 84);
		int blue = getInt(0, 84);
		return new Color(red, green, blue);
	}

	/**
	 * 生成一个随机字符
	 *
	 * @return
	 */
	private static char getRandomChar() {
		final String chs = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		return chs.charAt(r.nextInt(chs.length()));
	}

}
