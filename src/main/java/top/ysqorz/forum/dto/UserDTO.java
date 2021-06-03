package top.ysqorz.forum.dto;

import lombok.Data;
import top.ysqorz.forum.po.Role;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理界面返回的表单
 * @author 阿灿
 * @create 2021-05-14 22:47
 */
@Data
public class UserDTO {
    /**
     * 用户id
     */

    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;


    /**
     * 性别。0：男；1：女；3：保密
     */
    private Byte gender;

    /**
     * 头像
     */
    private String photo;


    /**
     * 注册时间
     */
    @Column(name = "register_time")
    private LocalDateTime registerTime;


    /**
     * 上一次登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;


    /**
     * 如果当前用户处于封禁，必有一条当前的小黑记录。
     * 可能当前用户之前也被封过，但是我们需要当前封禁的记录id
     */
    private Integer blackId;

    private List<Role> roles;
}
