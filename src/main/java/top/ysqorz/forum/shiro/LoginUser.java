package top.ysqorz.forum.shiro;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import top.ysqorz.forum.common.enumeration.Gender;
import top.ysqorz.forum.po.User;

import java.io.Serializable;

/**
 * 作为Shiro中Subject的Credentials，缓存在Redis中
 * @author passerbyYSQ
 * @create 2022-03-28 15:53
 */
@Data
@NoArgsConstructor
public class LoginUser implements Serializable {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String loginSalt;
    private Gender gender;
    private String photo;
    private String token;

    public LoginUser(User user, String token) {
        BeanUtils.copyProperties(user, this);
        this.token = token;
    }
}
