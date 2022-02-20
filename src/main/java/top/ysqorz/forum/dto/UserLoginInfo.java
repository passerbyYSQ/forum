package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import top.ysqorz.forum.po.User;

import java.time.LocalDateTime;

/**
 * 用户登录成功
 *
 * @author passerbyYSQ
 * @create 2021-06-02 14:27
 */
@Getter
@Setter
public class UserLoginInfo {
    private String token;
    private String username;
    private String email;
    private String phone; // 手机
    private Byte gender;
    private String photo; // 照片
    private Integer rewardPoints;
    private LocalDateTime registerTime;

    private String referer;

    // 角色信息...


    public UserLoginInfo() {

    }

    public UserLoginInfo(String token, User user, String referer) {
        this.token = token;
        this.referer = referer;
        BeanUtils.copyProperties(user, this);
    }
}
