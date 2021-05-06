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
@Table(name = "collect")
public class Collect {
    @Id
    private Integer id;

    /**
     * 用户id
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
}