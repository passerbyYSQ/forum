package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "`like`") // 表名 like 与mysql关键字冲突，此处需要重命名加上 ``
public class Like {
    @Id
    private Integer id;

    /**
     * 点赞的用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 帖子id
     */
    @Column(name = "post_id")
    private Integer postId;

    /**
     * 点赞时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Byte isRead;
}
