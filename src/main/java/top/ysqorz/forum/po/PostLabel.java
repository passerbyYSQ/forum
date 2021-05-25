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

    public PostLabel() {
    }

    public PostLabel(Integer postId, Integer labelId) {
        this.postId = postId;
        this.labelId = labelId;
    }
}
