package top.ysqorz.forum.dto;

import lombok.Data;
import top.ysqorz.forum.po.Blacklist;

import javax.validation.constraints.NotNull;

/**
 * 用于在改绑时检验用户
 * @author ligouzi
 * @create 2021-07-01 17:06
 */
@Data
public class CheckUserDTO {

    @NotNull
    private String oldEmail;
    @NotNull
    private String checkPassword;
    private String newPhone;
    private String newEmail;

}
