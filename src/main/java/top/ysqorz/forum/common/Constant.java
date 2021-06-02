package top.ysqorz.forum.common;

/**
 * 项目中的用到的常量
 *
 * @author passerbyYSQ
 * @create 2021-02-02 14:03
 */
public interface Constant {

    // 手机号的正则表达式
    String REGEX_PHONE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    // 匹配常见图片的base64编码的正则表达式
    String BASE64_REGEX =  "^(data:image/(jpg|jpeg|png);base64,)(\\S+)";

    // redis prefix captcha
    String REDIS_PREFIX_CAPTCHA = "captcha:";

}
