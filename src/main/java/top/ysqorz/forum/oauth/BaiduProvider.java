package top.ysqorz.forum.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.dto.BaiduUserDTO;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.OkHttpUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * baidu认证登录网络链接
 * https://pan.baidu.com/union/document/entrance#%E6%8E%A5%E5%85%A5%E6%B5%81%E7%A8%8B
 * @author ligouzi
 * @create 2021-06-21 10:57
 */
@Component
@ConfigurationProperties(prefix = "oauth.baidu")
public class BaiduProvider extends OauthProvider<BaiduUserDTO>{

    @Override
    public String joinAuthorizeUrl(String state, HttpServletResponse response) {
        return String.format("http://openapi.baidu.com/oauth/2.0/authorize" +
                "?response_type=code&client_id=%s&redirect_uri=%s&scope=basic,netdisk&state=%s&force_login=1",
                clientId, redirectUri, state);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        // 方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String body = OkHttpUtils.builder().url("https://openapi.baidu.com/oauth/2.0/token")
                // 有参数的话添加参数，可多个
                .addParam("grant_type", "authorization_code")
                .addParam("code", code)
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .get()
                .sync();
        return JsonUtils.jsonToNode(body).get("access_token").asText();
    }

    @Override
    protected BaiduUserDTO getOauthUser(String accessToken) throws IOException {
        String body = OkHttpUtils.builder().url("https://pan.baidu.com/rest/2.0/xpan/nas")
                .addParam("method", "uinfo")
                .addParam("access_token", accessToken)
                .get()
                .sync();
        return JsonUtils.jsonToObj(body, BaiduUserDTO.class);
    }
}