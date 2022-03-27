package top.ysqorz.forum.po;

import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "chat_friend_group")
public class ChatFriendGroup {
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 好友分组的名称
     */
    @Column(name = "group_name")
    private String groupName;
}