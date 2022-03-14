package top.ysqorz.forum.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import top.ysqorz.forum.dto.BaiduUserDTO;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.OkHttpUtils;

import java.io.IOException;

/**
 * https://pan.baidu.com/union/doc/0ksg0sbig
 *
 * @author ligouzi
 * @create 2021-06-21 10:57
 */
@Component
@ConfigurationProperties(prefix = "oauth.baidu")
public class BaiduProvider extends OauthProvider<BaiduUserDTO> {

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
