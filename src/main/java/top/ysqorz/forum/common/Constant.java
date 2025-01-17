package top.ysqorz.forum.common;

import java.time.Duration;

/**
 * 项目中的用到的常量
 *
 * @author passerbyYSQ
 * @create 2021-02-02 14:03
 */
public interface Constant {

    int WS_SERVER_PORT = 8081;

    // 手机号的正则表达式
    String REGEX_PHONE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";
    // 邮箱的正则表达式
    String REGEX_EMAIL= "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    // 匹配常见图片的base64编码的正则表达式
    String REGEX_BASE64 = "^(data:image/(jpg|jpeg|png);base64,)(\\S+)";

    // redis prefix captcha
    String REDIS_KEY_CAPTCHA = "captcha:";
    //
    String REDIS_KEY_OAUTH_STATE = "oauth:state:";
    // post:visit:postId:ip
    String REDIS_KEY_POST_VISIT = "post:visit:%d:%s";
    // im:ws:channelType:groupId
    String REDIS_KEY_IM_WS = "im:ws:%s:%s";
    // keypair:%s
    String REDIS_KEY_KEY_PAIR = "keypair:%s";

    // redis captcha 有效期：5分钟
    Duration DURATION_CAPTCHA = Duration.ofMinutes(5);
    //
    Duration DURATION_OAUTH_STATE = Duration.ofMinutes(3);
    Duration DURATION_POST_VISIT = Duration.ofMinutes(3);
    // jwt 的有效期：7天
    Duration DURATION_JWT = Duration.ofDays(7);
    Duration DURATION_KEY_PAIR = Duration.ofDays(7);

    // 每一页的记录数，默认值
    Integer PAGE_DATA_COUNT = 10;

    String token = "token";
}
