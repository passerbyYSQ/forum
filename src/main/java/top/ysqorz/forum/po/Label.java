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
@Table(name = "label")
public class Label {
    /**
     * 标签id
     */
    @Id
    private Integer id;

    /**
     * 标签名
     */
    @Column(name = "label_name")
    private String labelName;

    /**
     * 标签描述
     */
    private String description;

    /**
     * 帖子数
     */
    @Column(name = "post_count")
    private Integer postCount;
}