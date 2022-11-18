package top.ysqorz.forum.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ligouzi
 * @create 2021-06-21 10:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaiduUserDTO {

    private String uk;          // 用户ID

    @JsonProperty("vip_type")
    private Integer vipType;    // 会员类型，0普通用户、1普通会员、2超级会员

    @JsonProperty("baidu_name")
    private String baiduName;   // 百度账号

    @JsonProperty("netdisk_name")
    private String netDiskName; // 网盘账号

    @JsonProperty("avatar_url")
    private String avatarUrl;   // 头像地址

}
