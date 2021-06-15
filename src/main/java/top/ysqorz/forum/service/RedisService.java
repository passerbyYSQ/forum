package top.ysqorz.forum.service;

/**
 * @author passerbyYSQ
 * @create 2021-06-02 13:18
 */
public interface RedisService {

    /**
     * 保存验证码
     * @param key
     * @param captcha   验证码字符串
     */
    void saveCaptcha(String key, String captcha);

    /**
     * 取出验证码
     */
    String getCaptcha(String key);


    /**
     * 第三方授权回调时防止CSRF
     * @param key
     * @param state
     */
    void saveOauthState(String key, String state);

    String getOauthState(String key);

}
