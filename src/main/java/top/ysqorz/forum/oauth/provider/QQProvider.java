package top.ysqorz.forum.oauth.provider;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.ysqorz.forum.common.RestRequest;
import top.ysqorz.forum.common.enumeration.Gender;
import top.ysqorz.forum.oauth.AbstractOauthProvider;
import top.ysqorz.forum.oauth.dto.QQUserDTO;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.OkHttpUtils;

/**
 * <a href="https://wiki.connect.qq.com/">...</a>
 *
 * @author passerbyYSQ
 * @create 2021-06-15 11:14
 */
@Component("qq")
@ConfigurationProperties(prefix = "oauth.qq")
public class QQProvider extends AbstractOauthProvider<QQUserDTO> {

    @Override
    public String joinAuthorizeUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl("https://graph.qq.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("scope", "get_user_info")
                .queryParam("state", state)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    @Override
    public String getAccessToken(String code) {
        JSONObject data = RestRequest.builder().url("https://graph.qq.com/oauth2.0/token")
                .addParam("grant_type", "authorization_code")
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .addParam("code", code)
                .addParam("fmt", "json")
                .get(JSONObject.class);
        // 只需要access_token，无需要额外定义一个实体类接收
        return data.getStr("access_token");
    }

    @Override
    public QQUserDTO getOauthUser(String accessToken) {
        JSONObject data = RestRequest.builder().url("https://graph.qq.com/oauth2.0/me")
                .addParam("access_token", accessToken)
                .addParam("fmt", "json")
                .get(JSONObject.class);
        // openid是此网站上唯一对应用户身份的标识，网站可将此ID进行存储便于用户下次登录时辨识其身份，或将其与用户在网站上的原有账号进行绑定。
        String openId = data.getStr("openid");
        String userAppId = data.getStr("client_id");

        QQUserDTO qqUserDTO = RestRequest.builder().url("https://graph.qq.com/user/get_user_info")
                .addParam("access_token", accessToken)
                .addParam("oauth_consumer_key", userAppId)
                .addParam("openid", openId)
                .get(QQUserDTO.class);
        qqUserDTO.setOpenId(openId);
        return qqUserDTO;
    }

    @Override
    protected String getUniqueId(QQUserDTO oauthUser) {
        return oauthUser.getOpenId();
    }

    @Override
    protected void completeDbUser(QQUserDTO oauthUser, User dbUser) {
        dbUser.setUsername(oauthUser.getNickname())
                .setPhoto(oauthUser.getFigureUrlQQ1())
                .setEmail("")
                .setGender("男".equals(oauthUser.getGender()) ? Gender.MALE : Gender.FEMALE);
    }
}
