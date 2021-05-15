package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "role")
public class Role {
    /**
     * 由于权限新增和更新都公用一个模型，
     * 新增不需要传authorityId（数据库自增长生成）
     * 更新必须要传authorityId，所以需要使用分组校验
     */
    public interface Add {}
    public interface Update {}

    /**
     * 角色id
     */
    @NotNull(groups = Update.class) // 分组校验
    @Min(value = 0, groups = Update.class)
    @Id
    private Integer id;

    /**
     * 角色名
     */
    @NotEmpty
    @Size(min = 2, max = 32)
    @Column(name = "role_name")
    private String roleName;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
