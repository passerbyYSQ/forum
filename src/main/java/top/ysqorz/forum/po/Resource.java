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
@Table(name = "resource")
public class Resource {
    @Id
    private Integer id;

    /**
     * 权限名称
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 权限表达式
     */
    private String permission;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 路径
     */
    private String url;

    /**
     * 排序权重
     */
    @Column(name = "sort_weight")
    private Integer sortWeight;
}