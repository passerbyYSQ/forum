package top.ysqorz.forum.utils;

import java.util.Random;
import java.util.UUID;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 11:08
 */
public class RandomUtils {

    private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-+=";
    private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String numberChar = "0123456789";

    public static String generateStr(int length) {
        return generateStr(length, allChar);
    }

    public static String generateStrWithNumAndLetter(int length) {
        return generateStr(length, numberChar + letterChar);
    }

    /**
     * 产生指定长度的随机字符串（各种字符）
     *
     * @param length    长度要求
     * @return
     */
    public static String generateStr(int length, String source) {
        StringBuilder sbd = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sbd.append(allChar.charAt(random.nextInt(source.length())));
        }
        return sbd.toString();
    }


    /**
     * 生成一个uuid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
