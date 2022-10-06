package top.ysqorz.forum.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

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
    public interface Register {
    } // 注册

    public interface UpdatePassword {
    } // 更改密码

    @NotBlank(groups = Register.class)
    private String token;

    @Length(groups = Register.class, min = 3, max = 24)
    private String username;

    @NotBlank(groups = Register.class)
    private String captcha;

    @NotBlank(groups = Register.class)
    @Email(groups = Register.class)
    private String email;

    @NotBlank
    private String password; // 公用

    @NotBlank
    private String rePassword; // 公用

    @NotBlank(groups = UpdatePassword.class)
    private String newPassword;
}

