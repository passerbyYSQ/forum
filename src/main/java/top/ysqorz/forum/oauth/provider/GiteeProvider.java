package top.ysqorz.forum.oauth.provider;

import cn.hutool.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.ysqorz.forum.common.RestRequest;
import top.ysqorz.forum.common.enumeration.Gender;
import top.ysqorz.forum.oauth.AbstractOauthProvider;
import top.ysqorz.forum.oauth.dto.GiteeUserDTO;
import top.ysqorz.forum.po.User;

/**
 * gitee认证登录网络链接
 * <a href="https://gitee.com/api/v5/oauth_doc#/">...</a>
 *
 * @author 阿灿
 * @create 2021-06-11 13:58
 */
@Component("gitee")
@ConfigurationProperties(prefix = "oauth.gitee")
public class GiteeProvider extends AbstractOauthProvider<GiteeUserDTO> {

    @Override
    public String joinAuthorizeUrl(String state) {
        return UriComponentsBuilder.fromHttpUrl("https://gitee.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("scope", "user_info")
                .queryParam("state", state)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .toUriString();
    }

    @Override
    public String getAccessToken(String code) {
        JSONObject data = RestRequest.builder().url("https://gitee.com/oauth/token")
                .addParam("grant_type", "authorization_code")
                .addParam("client_id", clientId)
                .addParam("client_secret", clientSecret)
                .addParam("redirect_uri", redirectUri)
                .addParam("code", code)
                .post(JSONObject.class);
        return data.getStr("access_token");
    }

    @Override
    public GiteeUserDTO getOauthUser(String accessToken) {
        return RestRequest.builder().url("https://gitee.com/api/v5/user")
                .addParam("access_token", accessToken)
                .get(GiteeUserDTO.class);
    }

    @Override
    protected String getUniqueId(GiteeUserDTO oauthUser) {
        return oauthUser.getId();
    }

    @Override
    protected void completeDbUser(GiteeUserDTO oauthUser, User dbUser) {
        dbUser.setUsername(oauthUser.getName())
                .setPhoto(oauthUser.getAvatarUrl())
                .setEmail(oauthUser.getEmail() != null ? oauthUser.getEmail() : "")
                .setGender(Gender.SECRET);
    }
}
