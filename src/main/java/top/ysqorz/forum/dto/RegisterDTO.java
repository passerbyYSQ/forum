package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 *
 * 注册表单
 * @author 阿灿
 * @create 2021-06-03 15:08
 */
@Getter
@Setter
public class RegisterDTO {
    @NotEmpty
    private String token;

    @NotEmpty
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String rePassword;

    @NotEmpty
    private String captcha;
}

