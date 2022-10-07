package top.ysqorz.forum.oauth.provider;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.ysqorz.forum.common.enumeration.Gender;
import top.ysqorz.forum.oauth.AbstractOauthProvider;
import top.ysqorz.forum.oauth.dto.GiteeUserDTO;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.OkHttpUtils;

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
    public GiteeUserDTO getOauthUser(String accessToken) {
        // 方法顺序按照这种方式，切记选择post/get一定要放在倒数第二，同步或者异步倒数第一，才会正确执行
        String body = OkHttpUtils.builder().url("https://gitee.com/api/v5/user")
                // 有参数的话添加参数，可多个
                .addParam("access_token", accessToken)
                .get()
                .sync();
        return JsonUtils.jsonToObj(body, GiteeUserDTO.class);
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
