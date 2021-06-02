package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * 登录表单
 *
 * @author passerbyYSQ
 * @create 2021-06-02 15:09
 */
@Getter
@Setter
public class LoginDTO {
    @NotEmpty
    private String token;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String captcha;
}
