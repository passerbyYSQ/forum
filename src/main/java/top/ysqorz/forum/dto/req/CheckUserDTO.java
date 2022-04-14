package top.ysqorz.forum.dto.req;

import lombok.Data;

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

    private String poFile; // 第三方账号的类型

}
