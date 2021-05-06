package top.ysqorz.forum.po;

import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "role")
public class Role {
    /**
     * 角色id
     */
    @Id
    private Integer id;

    /**
     * 角色名
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 描述
     */
    private String description;
}