package top.ysqorz.forum.dto.req;

import lombok.Data;
import top.ysqorz.forum.common.enumeration.Oauth2App;

import javax.validation.constraints.NotBlank;

/**
 * 用于在改绑时检验用户
 * @author ligouzi
 * @create 2021-07-01 17:06
 */
@Data
public class CheckUserDTO {

    @NotBlank
    private String oldEmail;
    @NotBlank
    private String checkPassword;

    private String rePassword;
    private String newEmail;

    private String newPhone;

    private Oauth2App oauth2App; // 第三方账号的类型

}
