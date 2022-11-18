package top.ysqorz.forum.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author passerbyYSQ
 * @create 2021-06-15 11:16
 */
@Getter
@Setter
public class QQUserDTO {

    private String openId;

    private String nickname;

    @JsonProperty("figureurl_qq_1")
    private String figureUrlQQ1; // 大小为40×40像素的QQ头像URL。

    @JsonProperty("figureurl_qq_2")
    private String figureUrlQQ2; // 不一定有。大小为100×100像素的QQ头像URL

    private String gender; // 性别。 "男"

}
