package top.ysqorz.forum.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubUserDTO {
    @JsonProperty("id")
    private String id;          // 用户ID

    @JsonProperty("login")
    private String login;   // 百度账号

    @JsonProperty("avatar_url")
    private String avatarUrl;   // 头像地址

}
