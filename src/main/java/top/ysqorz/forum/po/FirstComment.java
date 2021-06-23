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
@Table(name = "first_comment")
public class FirstComment {

    /**
     * 评论id
     */
    @Id
    private Integer id;

    /**
     * 发送者id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 所属帖子
     */
    @Column(name = "post_id")
    private Integer postId;

    /**
     * 楼层编号
     */
    @Column(name = "floor_num")
    private Integer floorNum;

    /**
     * 该一级评论下二级评论的数量
     */
    @Column(name = "second_comment_count")
    private Integer secondCommentCount;

    /**
     * 发布的时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 评论内容
     */
    private String content;
}
