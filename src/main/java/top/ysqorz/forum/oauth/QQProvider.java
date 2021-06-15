package top.ysqorz.forum.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import top.ysqorz.forum.dto.QQUserDTO;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.OkHttpUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * https://wiki.connect.qq.com/
 * @author passerbyYSQ
 * @create 2021-06-15 11:14
 */
public class QQProvider extends OauthProvider<QQUserDTO> {

    @Override
    public String joinAuthorizeUrl(String state, HttpServletResponse response) {
        return String.format("https://graph.qq.com/oauth2.0/authorize?client_id=%s&redirect_uri=%s" +
                "state=%s&response_type=code&scope=get_user_info", clientId, redirectUri, state);
    }

    @Override
    protected String getAccessToken(String code) throws IOException {
        // 方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String body = OkHttpUtils.builder().url("https://graph.qq.com/oauth2.0/token")
                // 有参数的话添加参数，可多个
                .addParam("grant_type", "authorization_code")
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .addParam("code", code)
                .addParam("fmt", "json")
                .get()
                .sync();
        // 只需要access_token，无需要额外定义一个实体类接收
        return JsonUtils.jsonToNode(body).get("access_token").asText();
    }

    @Override
    protected QQUserDTO getOauthUser(String accessToken) throws IOException {
        String body = OkHttpUtils.builder().url("https://graph.qq.com/oauth2.0/me")
                // 有参数的话添加参数，可多个
                .addParam("access_token", accessToken)
                .addParam("fmt", "json")
                .get()
                .sync();
        JsonNode jsonNode = JsonUtils.jsonToNode(body);
        // openid是此网站上唯一对应用户身份的标识，网站可将此ID进行存储便于用户下次登录时辨识其身份，或将其与用户在网站上的原有账号进行绑定。
        String openId = jsonNode.get("openid").asText();
        String userAppId = jsonNode.get("client_id").asText();

        String userInfo = OkHttpUtils.builder().url("https://graph.qq.com/user/get_user_info")
                // 有参数的话添加参数，可多个
                .addParam("access_token", accessToken)
                .addParam("oauth_consumer_key", userAppId)
                .addParam("openid", openId)
                .get()
                .sync();
        return JsonUtils.jsonToObj(userInfo, QQUserDTO.class);
    }
}
