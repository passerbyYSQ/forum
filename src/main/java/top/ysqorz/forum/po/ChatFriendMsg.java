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
@Table(name = "chat_friend_msg")
public class ChatFriendMsg {
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
     * 消息的内容
     */
    private String content;

    /**
     * 是否已签收。0：未签收；1：已签收
     */
    @Column(name = "sign_flag")
    private Byte signFlag;

    /**
     * 发送时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
