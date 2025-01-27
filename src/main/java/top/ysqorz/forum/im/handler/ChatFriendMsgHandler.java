package top.ysqorz.forum.im.handler;

import top.ysqorz.forum.dao.ChatFriendMsgMapper;
import top.ysqorz.forum.im.entity.AsyncInsertTask;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.ChatFriendMsg;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2022-04-03 16:52
 */
public class ChatFriendMsgHandler extends NonFunctionalMsgHandler<ChatFriendMsg> {

    public ChatFriendMsgHandler() {
        super(MsgType.CHAT_FRIEND, ChannelType.CHAT);
    }

    @Override
    protected Set<String> queryServersChannelLocated(ChatFriendMsg msg) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(this.getChannelType(), msg.getReceiverId().toString());
    }

    @Override
    protected void doPush(ChatFriendMsg msg, String sourceChannelId) {
        this.channelMap.pushToGroup(this.getMsgType(), msg, sourceChannelId, msg.getReceiverId().toString());
    }

    @Override
    protected AsyncInsertTask<ChatFriendMsg> createAsyncInsertTask(ChatFriendMsg msg) {
        ChatFriendMsgMapper mapper = SpringUtils.getBean(ChatFriendMsgMapper.class);
        return new AsyncInsertTask<>(mapper, msg);
    }
}
