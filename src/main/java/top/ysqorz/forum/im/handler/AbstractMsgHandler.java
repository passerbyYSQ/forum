package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.MsgHandler;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author passerbyYSQ
 * @create 2022-01-11 23:57
 */
@Getter
@Slf4j
public abstract class AbstractMsgHandler<DataType> implements MsgHandler<DataType> {
    // 能够处理的消息类型
    private final MsgType msgType;
    // 通道类型
    private final ChannelType channelType;
    // 该类型消息的所有通道。子类使用
    private ChannelMap channelMap;
    // 线程池异步消费数据库操作。子类使用
    private Executor executor;

    public AbstractMsgHandler(MsgType msgType) {
        this(msgType, null);
    }

    public AbstractMsgHandler(MsgType msgType, ChannelType channelType) {
        this.msgType = msgType;
        this.channelType = channelType;
    }

    @Override
    public boolean support(MsgModel msg, Channel channel) {
        return matchMsgType(msg) && authenticate(msg, channel);
    }

    @Override
    public boolean handle(MsgModel msg, Channel channel) {
        if (!support(msg, channel)) {
            return false;

        }
        channel.attr(IMUtils.ALL_IDLE_KEY).set(0);  // 有合法消息可消费，重置空闲次数
        DataType data = transformData(msg); // data is JsonNode
        if (Objects.isNull(data)) {
            return false;
        }
        saveData(data);
        return doHandle(msg, data, channel);
    }

    protected DataType transformData(MsgModel msg) {
        Class<DataType> clazz = getDataClass();
        if (MsgModel.class.isAssignableFrom(clazz)) {
            return clazz.cast(msg);
        }
        // 转换data部分
        // 由于远程转发，序列化成Object对象后实际类型是LinkedHashMap，这个时候强转类型报错
        // java.lang.ClassCastException: Cannot cast java.util.LinkedHashMap to top.ysqorz.forum.po.DanmuMsg
        // clazz.cast(msg.getData());
        // 解决方法：LinkedHashMap => JsonNode => 指定类型
        DataType data = JsonUtils.nodeToObj(msg.transform2DataNode(), clazz);
        msg.setData(data); // completed data
        return data;
    }

    @Override
    public boolean push(MsgModel msg, String sourceChannelId) { // msg.data is completed
        if (!matchMsgType(msg)) {
            return false;
        }
        DataType data = transformData(msg); // data is DataType
        if (Objects.isNull(data)) {
            return false;
        }
        return doPush(data, sourceChannelId);
    }

    protected boolean matchMsgType(MsgModel msg) {
        return msgType != null && msgType.name().equalsIgnoreCase(msg.getMsgType());
    }

    protected boolean authenticate(MsgModel msg, Channel channel) {
        return checkBound(channel) || checkToken(msg);
    }

    protected boolean checkBound(Channel channel) {
        return channelMap != null && channelMap.isBound(channel); // 功能类型的消息，channelMap为空
    }

    protected final boolean checkToken(MsgModel msg) {
        JsonNode dataNode = msg.transform2DataNode();
        if (dataNode == null || !dataNode.has("token")) {
            return false;
        }
        String token = dataNode.get("token").asText();
        String userId = JwtUtils.getClaimByKey(token, "userId");
        User user = getUserById(Integer.valueOf(userId));
        return user != null && JwtUtils.verifyJwt(token, user.getLoginSalt());
    }

    protected User getUserById(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        return userService.getUserById(userId);
    }


    @Override
    public void setChannelMap(ChannelMap channelMap) {
        this.channelMap = channelMap;
    }

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    protected void saveData(DataType data) {
    }

    protected abstract boolean doHandle(MsgModel msg, DataType data, Channel channel);

    protected abstract boolean doPush(DataType data, String sourceChannelId);

}
