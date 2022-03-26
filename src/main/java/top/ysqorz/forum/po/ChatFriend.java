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
@Table(name = "chat_friend")
public class ChatFriend {
    @Id
    private Integer id;

    /**
     * 我的用户id
     */
    @Column(name = "my_id")
    private Integer myId;

    /**
     * 好友的用户id
     */
    @Column(name = "friend_id")
    private Integer friendId;

    /**
     * 好友的备注名
     */
    private String alias;

    /**
     * 成为好友额时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
