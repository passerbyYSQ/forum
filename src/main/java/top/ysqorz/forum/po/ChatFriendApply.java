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
@Table(name = "chat_friend_apply")
public class ChatFriendApply {
    @Id
    private Integer id;

    /**
     * 发送者的用户id
     */
    @Column(name = "sender_id")
    private Integer senderId;

    /**
     * 接收者的用户id
     */
    @Column(name = "receiver_id")
    private Integer receiverId;

    /**
     * 好友申请的内容
     */
    private String content;

    /**
     * 状态。0：拒绝；1：同意；2：忽略
     */
    private Byte status;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    private LocalDateTime applyTime;
}
