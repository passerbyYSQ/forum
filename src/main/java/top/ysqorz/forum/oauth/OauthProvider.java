package top.ysqorz.forum.oauth;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author passerbyYSQ
 * @create 2022-10-07 22:16
 */
public interface OauthProvider<T> {
    String redirectAuthorize(String referer);

    String getAccessToken(String code);

    T getOauthUser(String code);

    String oauthCallback(String state, String code, HttpServletResponse resp);
}
