package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.ysqorz.forum.common.TreeNode;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Table(name = "resource")
public class Resource extends TreeNode<Integer> implements Serializable {

    /**
     * 由于权限新增和更新都公用一个模型，
     * 新增不需要传authorityId（数据库自增长生成）
     * 更新必须要传authorityId，所以需要使用分组校验
     */
    public interface Add {}
    public interface Update {}

    @NotNull(groups = Update.class) // 分组校验
    @Min(value = 0, groups = Update.class)
//    @JsonProperty("authorityId")
    @Id
    private Integer id;

    /**
     * 权限名称
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 资源名称
     */
//    @JsonProperty("authorityName")
    @NotEmpty
    @Size(min = 2, max = 32)
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路径
     */
    private String url;

    /**
     * 权限表达式
     */
    @NotEmpty
    private String permission;

    /**
     * 类型。0：菜单，1：按钮
     */
    @NotNull
    @Max(1) // <=1
    @Min(0) // >=0
    private Byte type;

    /**
     * 排序权重
     */
    @Column(name = "sort_weight")
    private Integer sortWeight;
}
