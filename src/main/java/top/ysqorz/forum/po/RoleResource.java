package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "role_resource")
public class RoleResource {
    @Id
    private Integer id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Integer roleId;

    /**
     * 权限id
     */
    @Column(name = "resource_id")
    private Integer resourceId;

    public RoleResource() {
    }

    public RoleResource(Integer roleId, Integer resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }
}
