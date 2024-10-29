package top.ysqorz.forum.oauth.provider;

import cn.hutool.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.ysqorz.forum.common.RestRequest;
import top.ysqorz.forum.common.enumeration.Gender;
import top.ysqorz.forum.oauth.AbstractOauthProvider;
import top.ysqorz.forum.oauth.dto.BaiduUserDTO;
import top.ysqorz.forum.oauth.dto.GithubUserDTO;
import top.ysqorz.forum.po.User;

/**
 * <a href="https://pan.baidu.com/union/doc/0ksg0sbig">...</a>
 *
 * @author ligouzi
 * @create 2021-06-21 10:57
 */
@Component("github")
@ConfigurationProperties(prefix = "oauth.github")
public class GithubProvider extends AbstractOauthProvider<GithubUserDTO> {

    @Override
    public String joinAuthorizeUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("scope", "user") // 不能只填 basic，否则会获取不到用户信息
                .queryParam("state", state)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    @Override
    public String getAccessToken(String code) {
        JSONObject data = RestRequest.builder().url("https://github.com/login/oauth/access_token")
                .addHeader("Accept","application/json")
                .addParam("code", code)
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .get(JSONObject.class);
        return data.getStr("access_token");
    }

    @Override
    public GithubUserDTO getOauthUser(String accessToken) {
        return RestRequest.builder().url("https://api.github.com/user")
                .addHeader("Authorization","Bearer " + accessToken)
                .addParam("access_token", accessToken)
                .get(GithubUserDTO.class);
    }

    @Override
    protected String getUniqueId(GithubUserDTO oauthUser) {
        return oauthUser.getId();
    }

    @Override
    protected void completeDbUser(GithubUserDTO oauthUser, User dbUser) {
        dbUser.setUsername(oauthUser.getLogin())
                .setPhoto(oauthUser.getAvatarUrl())
                .setEmail("")
                .setGender(Gender.SECRET);
    }
}
