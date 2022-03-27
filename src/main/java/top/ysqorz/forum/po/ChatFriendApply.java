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
     * 好友分组的id。如果为null表示未分组
     */
    @Column(name = "friend_group_id")
    private Integer friendGroupId;

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