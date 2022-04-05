package top.ysqorz.forum.im.handler;

import top.ysqorz.forum.dto.resp.ChatNotificationDTO;
import top.ysqorz.forum.im.entity.*;
import top.ysqorz.forum.service.ChatService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2022-04-05 15:49
 */
public class ChatNotificationHandler extends NonFunctionalMsgHandler<ChatNotificationDTO> implements OnOffLineAware {

    public ChatNotificationHandler() {
        super(MsgType.CHAT_NOTIFICATION, ChannelType.CHAT);
    }

    @Override
    public void online(String token) {
        pushNotification("online", token);
    }

    @Override
    public void offline(String token) {
        pushNotification("offline", token);
    }

    private void pushNotification(String action, String token) {
        Integer loginUserId = Integer.valueOf(JwtUtils.getClaimByKey(token, "userId"));
        ChatNotificationDTO notification = new ChatNotificationDTO();
        notification.setAction(action)
                .setSenderId(loginUserId)
                .setTime(LocalDateTime.now());
        ChatService chatService = SpringUtils.getBean(ChatService.class);
        Set<Integer> friendIdSet = chatService.getAllFriendUserIds(loginUserId);
        MsgModel msgModel = new MsgModel(this.getMsgType(), this.getChannelType());
        for (Integer friendId : friendIdSet) {
            notification.setReceiverId(friendId);
            msgModel.setData(notification);
            this.doRemoteDispatch(msgModel, null, token);
        }
    }

    @Override
    protected Set<String> queryServersChannelLocated(ChatNotificationDTO data) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(this.getChannelType(), data.getReceiverId().toString());
    }

    @Override
    protected void doPush(ChatNotificationDTO data, String sourceChannelId) {
        this.channelMap.pushToGroup(this.getMsgType(), data, sourceChannelId, data.getReceiverId().toString());
    }

    @Override
    protected AsyncInsertTask createAsyncInsertTask(ChatNotificationDTO data) {
        return null;
    }
}
