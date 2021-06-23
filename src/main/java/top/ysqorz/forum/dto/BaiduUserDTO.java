package top.ysqorz.forum.dto;

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
    private Integer vip_type;    // 会员类型，0普通用户、1普通会员、2超级会员
    private String baidu_name;   // 百度账号
    private String netdisk_name; // 网盘账号
    private String avatar_url;   // 头像地址

}