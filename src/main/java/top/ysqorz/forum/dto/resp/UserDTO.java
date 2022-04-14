package top.ysqorz.forum.dto.resp;

import lombok.Data;
import top.ysqorz.forum.po.Role;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理界面返回的表单
 * @author 阿灿
 * @create 2021-05-14 22:47
 */
@Data
public class UserDTO {

    private Integer id;
    private String username;
    private String email;
    private String phone;
    private Byte gender;
    private String photo;
    private LocalDateTime registerTime;
    private LocalDateTime lastLoginTime;
    private Integer blackId;
    private List<Role> roles;
}
