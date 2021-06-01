package top.ysqorz.forum.utils;

import java.util.Random;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 11:08
 */
public class RandomUtil {

    private static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()-+=";
    private static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String numberChar = "0123456789";

    /**
     * 产生指定长度的随机字符串（各种字符）
     *
     * @param length    长度要求
     * @return
     */
    public static String generateStr(int length) {
        StringBuilder sbd = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sbd.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sbd.toString();
    }

    public static void main(String[] args) {
        System.out.println(RandomUtil.generateStr(8));
    }

}
