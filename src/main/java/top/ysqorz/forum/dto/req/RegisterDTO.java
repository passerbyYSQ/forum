package top.ysqorz.forum.dto.req;

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
    // 分组校验
    public interface Register {} // 注册
    public interface UpdatePassword {} // 更改密码

    @NotEmpty(groups = Register.class)
    private String token;

    @NotEmpty(groups = Register.class)
    private String username;

    @NotEmpty(groups = Register.class)
    private String captcha;

    @NotEmpty(groups = Register.class)
    @Email(groups = Register.class)
    private String email;

    @NotEmpty
    private String password; // 公用

    @NotEmpty
    private String rePassword; // 公用

    @NotEmpty(groups = UpdatePassword.class)
    private String newPassword;
}

