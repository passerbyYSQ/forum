package top.ysqorz.forum.dto.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import top.ysqorz.forum.po.User;

/**
 * @author passerbyYSQ
 * @create 2022-03-26 16:52
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor // 补充空参构造
public class ChatUserCardDTO {
    private Integer userId;
    private String photo;
    private String username;
    private Boolean isChatFriend;
    private String alias; // 好友备注名
    private Byte gender;
    private String status; // 是否在线。online，offline
    private String desc; // 描述

    public ChatUserCardDTO(User user) {
        BeanUtils.copyProperties(user, this);
        this.userId = user.getId();
        this.desc = user.getDescription();
    }
}
