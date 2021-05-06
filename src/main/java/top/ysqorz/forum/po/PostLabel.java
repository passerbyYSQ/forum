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
@Table(name = "post_label")
public class PostLabel {
    @Id
    private Integer id;

    /**
     * 帖子id
     */
    @Column(name = "post_id")
    private Integer postId;

    /**
     * 标签id
     */
    @Column(name = "label_id")
    private Integer labelId;
}