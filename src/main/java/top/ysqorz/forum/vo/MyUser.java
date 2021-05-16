package top.ysqorz.forum.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author 阿灿
 * @create 2021-05-14 22:47
 */
@Data
public class MyUser {
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
     * 起始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;
}
