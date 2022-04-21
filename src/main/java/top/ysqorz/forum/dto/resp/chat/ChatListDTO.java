package top.ysqorz.forum.dto.resp.chat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.ysqorz.forum.po.User;

import java.util.List;
import java.util.Map;

/**
 * 在线聊天页面初始数据
 *
 * @author passerbyYSQ
 * @create 2022-03-30 19:51
 */
@Data
@Accessors(chain = true)
public class ChatListDTO {
    private ChatFriendDTO mine;
    private List<ChatFriendGroupDTO> friend;
    private List<ChatFriendDTO> group;

    @Data
    @Accessors(chain = true)
    public static class ChatFriendGroupDTO {
        private Integer id;
        private String groupname;
        private List<ChatFriendDTO> list; // 一对多映射
    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    public static class ChatFriendDTO {
        private Integer id;
        private String username;
        private String alias;
        private String avatar;
        private String sign;
        private String status; // online, offline
        private Map<String, Object> extra;

        public ChatFriendDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.avatar = user.getPhoto();
            this.sign = user.getDescription();
        }
    }

    @Data
    @Accessors(chain = true)
    public static class ChatGroupDTO {
        private Integer id;
        private String groupname;
        private String avatar;
    }
}
