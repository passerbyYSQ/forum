package top.ysqorz.forum.dto.resp.chat;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 未签收的好友聊天消息
 *
 * @author passerbyYSQ
 * @create 2022-04-22 0:35
 */
@Data
@Accessors(chain = true)
public class NotSignedChatFriendMsg {
    private String id; // 消息id
    private Friend sender; // 消息的发送者
    private String content;  // 消息的内容
    private LocalDateTime createTime;

    @Data
    public static class Friend {
        private Integer id;
        private String username;
        private String avatar;
    }
}
