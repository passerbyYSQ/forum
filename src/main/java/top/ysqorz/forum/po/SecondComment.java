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
@Table(name = "second_comment")
public class SecondComment {
    @Id
    private Integer id;

    /**
     * 发送者的用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 所属的一级评论id
     */
    @Column(name = "first_comment_id")
    private Integer firstCommentId;

    /**
     * 引用（回复）的二级评论id。如果为空，说明回复的是一级评论
     */
    @Column(name = "quote_second_comment_id")
    private Integer quoteSecondCommentId;

    /**
     * 发布时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 评论的内容
     */
    private String content;
}