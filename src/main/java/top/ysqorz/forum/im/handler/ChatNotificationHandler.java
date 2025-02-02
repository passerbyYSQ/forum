package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.dto.resp.chat.ChatNotificationDTO;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
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
public class ChatNotificationHandler extends RemoteMsgHandlerProxy<ChatNotificationDTO> implements OnOffLineAware {

    public ChatNotificationHandler() {
        super(MsgType.CHAT_NOTIFICATION, ChannelType.CHAT);
    }

    @Override
    public void online(Channel channel) {
        pushNotification("online", channel);
    }

    @Override
    public void offline(Channel channel) {
        pushNotification("offline", channel);
    }

    private void pushNotification(String action, Channel channel) {
        String token = IMUtils.getTokenFromChannel(channel);
        Integer loginUserId = Integer.valueOf(JwtUtils.getClaimByKey(token, "userId"));
        ChatNotificationDTO notification = new ChatNotificationDTO();
        notification.setAction(action)
                .setSenderId(loginUserId)
                .setTime(LocalDateTime.now());
        ChatService chatService = SpringUtils.getBean(ChatService.class);
        Set<Integer> friendIdSet = chatService.getAllFriendUserIds(loginUserId);
        MsgModel msgModel = new MsgModel(getMsgType(), getChannelType());
        for (Integer friendId : friendIdSet) {
            notification.setReceiverId(friendId);
            msgModel.setData(notification);
            doHandle(msgModel, notification, channel);
        }
    }

    @Override
    protected Set<String> queryServersChannelLocated(ChatNotificationDTO data) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(getChannelType(), data.getReceiverId().toString());
    }

    @Override
    protected boolean doPush(ChatNotificationDTO data, String sourceChannelId) {
        getChannelMap().pushToGroup(getMsgType(), data, sourceChannelId, data.getReceiverId().toString());
        return true;
    }

    @Override
    public Class<ChatNotificationDTO> getDataClass() {
        return ChatNotificationDTO.class;
    }
}
