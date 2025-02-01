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
public class ChatFriendMsgHandler extends RemoteMsgHandlerProxy<ChatFriendMsg> {

    public ChatFriendMsgHandler() {
        super(MsgType.CHAT_FRIEND, ChannelType.CHAT);
    }

    @Override
    protected Set<String> queryServersChannelLocated(ChatFriendMsg data) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(getChannelType(), data.getReceiverId().toString());
    }

    @Override
    protected boolean doPush(ChatFriendMsg data, String sourceChannelId) {
        getChannelMap().pushToGroup(getMsgType(), data, sourceChannelId, data.getReceiverId().toString());
        return true;
    }

    @Override
    protected void saveData(ChatFriendMsg data) {
        ChatFriendMsgMapper mapper = SpringUtils.getBean(ChatFriendMsgMapper.class);
        getExecutor().execute(new AsyncInsertTask<>(mapper, data));
    }

    @Override
    public Class<ChatFriendMsg> getDataClass() {
        return ChatFriendMsg.class;
    }
}
