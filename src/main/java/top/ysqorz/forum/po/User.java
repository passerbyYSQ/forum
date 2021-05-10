package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "user")
public class User {
    /**
     * 用户id
     */
    @Id
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
     * 密码
     */
    private String passsword;

    /**
     * 登录密码的随机盐
     */
    @Column(name = "login_salt")
    private String loginSalt;

    /**
     * 生成jwt的随机盐
     */
    @Column(name = "jwt_salt")
    private String jwtSalt;

    /**
     * 性别。0：男；1：女；3：保密
     */
    private Byte gender;

    /**
     * 头像
     */
    private String photo;

    /**
     * 个性签名
     */
    private String description;

    /**
     * 生日
     */
    private LocalDate birth;

    /**
     * 位置
     */
    private String position;

    /**
     * 注册时间
     */
    @Column(name = "register_time")
    private LocalDateTime registerTime;

    /**
     * 上一次更新时间
     */
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    /**
     * 上一次登录时间
     */
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    /**
     * 积分
     */
    @Column(name = "reward_points")
    private Integer rewardPoints;

    /**
     * 粉丝数
     */
    @Column(name = "fans_count")
    private Integer fansCount;
}
