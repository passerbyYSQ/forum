package top.ysqorz.forum.common;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * 第三方授权接口
 *
 * @author 阿灿
 * @create 2021-06-13 20:57
 */
@Getter
@Setter
public abstract class OauthProvider<T> {

    protected String clientId;

    protected String clientSecret;

    protected String redirectUri;


    /**
     * 获取Token
     *
     * @param code
     * @return
     */
    public abstract String getAccessToken(String code) throws IOException;

    /**
     * 获取返回用户信息，自行封装到T
     *
     * @param accessToken
     * @return
     */
    public abstract T getUser(String accessToken) throws IOException;
}
