package top.ysqorz.forum.utils;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.common.OauthProvider;
import top.ysqorz.forum.dto.GiteeUserDTO;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 阿灿
 * @create 2021-06-11 13:58
 * gitee认证登录网络链接
 */
@Component
@ConfigurationProperties(prefix = "oauth.gitee")
public class GiteeProvider extends OauthProvider<GiteeUserDTO> {

    @Override
    public String joinAuthorizeUrl(String referer, HttpServletResponse response) {
        return String.format("https://gitee.com/oauth/authorize" +
                        "?client_id=%s&redirect_uri=%s&state=%s&response_type=code&scope=user_info",
                clientId, redirectUri, referer);
    }

    @Override
    protected String getAccessTokenByCode(String code) {
        // 方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String body = OkHttpUtils.builder().url("https://gitee.com/oauth/token")
                // 有参数的话添加参数，可多个
                .addParam("grant_type", "authorization_code")
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .addParam("code", code)
                .post(true)
                .sync();
        String token = body.split(",")[0].split(":")[1];
        return token.substring(1, token.length() - 1); // 此上述token去除“”后的值

    }

    @Override
    protected GiteeUserDTO getUserByAccessToken(String accessToken) {
        // 方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String body = OkHttpUtils.builder().url("https://gitee.com/api/v5/user")
                // 有参数的话添加参数，可多个
                .addParam("access_token", accessToken)
                .get()
                .sync();
        return JsonUtils.jsonToObj(body, GiteeUserDTO.class);
    }

}
