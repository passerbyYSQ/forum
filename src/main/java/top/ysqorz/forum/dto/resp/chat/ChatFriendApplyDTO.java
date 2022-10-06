package top.ysqorz.forum.dto.resp.chat;

import lombok.Data;
import lombok.experimental.Accessors;
import top.ysqorz.forum.common.enumeration.ApplyStatus;

import java.time.LocalDateTime;

/**
 * 消息盒子的好友申请通知
 *
 * @author passerbyYSQ
 * @create 2022-03-29 16:04
 */
@Data
@Accessors(chain = true)
public class ChatFriendApplyDTO {
    private Integer applyId; // 好友申请的id，用于签收
    // 如果是我处理的，则发送者id不为空；如果是我发送的，则接收者id不为空
    private Integer senderId;
    private Integer receiverId;
    private String username;
    private String photo;
    private String sign; // 用户描述，用作签名
    private String content; // 好友申请的内容
    private ApplyStatus status; // 状态。null：未处理，0：拒绝；1：同意；2：忽略；
    // 如果是我处理的，这是发送者申请的时间；如果是我发送的，这是接收者处理申请的时间
    private LocalDateTime modifyTime;
}
