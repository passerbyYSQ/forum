package top.ysqorz.forum.po;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "admin_role")
public class AdminRole {
    @Id
    private Integer id;

    /**
     * 管理员id
     */
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 分配角色的时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}