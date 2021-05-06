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
@Table(name = "comment_notification")
public class CommentNotification {
    /**
     * 通知id
     */
    @Id
    private Integer id;

    /**
     * 发送者id
     */
    @Column(name = "sender_id")
    private Integer senderId;

    /**
     * 接收者id
     */
    @Column(name = "receiver_id")
    private Integer receiverId;

    /**
     * 通知类型。0：主题帖被回复，1：一级评论被回复，2：二级评论被回复
     */
    @Column(name = "comment_type")
    private Byte commentType;

    /**
     * 被回复的id（可能是主题帖、一级评论、二级评论，根据评论类型来判断）
     */
    @Column(name = "replied_id")
    private Integer repliedId;

    /**
     * 来自于哪条评论（可能是一级评论、二级评论）
     */
    @Column(name = "comment_id")
    private Integer commentId;

    /**
     * 时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Byte isRead;
}