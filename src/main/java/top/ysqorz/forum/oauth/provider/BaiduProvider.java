package top.ysqorz.forum.oauth.provider;

import cn.hutool.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.ysqorz.forum.common.RestRequest;
import top.ysqorz.forum.common.enumeration.Gender;
import top.ysqorz.forum.oauth.AbstractOauthProvider;
import top.ysqorz.forum.oauth.dto.BaiduUserDTO;
import top.ysqorz.forum.po.User;

/**
 * <a href="https://pan.baidu.com/union/doc/0ksg0sbig">...</a>
 *
 * @author ligouzi
 * @create 2021-06-21 10:57
 */
@Component("baidu")
@ConfigurationProperties(prefix = "oauth.baidu")
public class BaiduProvider extends AbstractOauthProvider<BaiduUserDTO> {

    @Override
    public String joinAuthorizeUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl("http://openapi.baidu.com/oauth/2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("scope", "basic,netdisk") // 不能只填 basic，否则会获取不到用户信息
                .queryParam("state", state)
                .queryParam("display", "popup")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    @Override
    public String getAccessToken(String code) {
        JSONObject data = RestRequest.builder().url("https://openapi.baidu.com/oauth/2.0/token")
                .addParam("grant_type", "authorization_code")
                .addParam("code", code)
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .get(JSONObject.class);
        return data.getStr("access_token");
    }

    @Override
    public BaiduUserDTO getOauthUser(String accessToken) {
        return RestRequest.builder().url("https://pan.baidu.com/rest/2.0/xpan/nas")
                .addParam("method", "uinfo")
                .addParam("access_token", accessToken)
                .get(BaiduUserDTO.class);
    }

    @Override
    protected String getUniqueId(BaiduUserDTO oauthUser) {
        return oauthUser.getUk();
    }

    @Override
    protected void completeDbUser(BaiduUserDTO oauthUser, User dbUser) {
        dbUser.setUsername(oauthUser.getBaiduName())
                .setPhoto(oauthUser.getAvatarUrl())
                .setEmail("")
                .setGender(Gender.SECRET);
    }
}
