package top.ysqorz.forum.im.entity;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;
import top.ysqorz.forum.utils.JsonUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道类型与消息消类型有差异：
 * 1. 通道类型以消息的业务类型划分
 * 2. 消息类型不仅以业务划分，还可能有一些基础类型的消息(比如说：BIND类型)
 * 例：有BIND类型的Handler和消息，但没有BIND类型的通道
 *
 * @author passerbyYSQ
 * @create 2022-01-12 0:00
 */
@Data
public class ChannelMap {
    // 通道的业务类型。如：弹幕、单聊、群聊。
    private MsgType type; // channelType
    // 该业务类型的所有通道 userId --> Set<Channel>
    private Map<String, Set<Channel>> channelMap = new ConcurrentHashMap<>();

    public ChannelMap(MsgType type) {
        this.type = type;
    }

    public void bind(String userId, Channel channel) {
        Set<Channel> channels = channelMap.get(userId);
        if (channels == null) {
            channelMap.put(userId, new HashSet<>());
        } else {
            channels.add(channel);
        }
    }

    public void pushExceptMe(Object data, Channel meChannel) {
        Set<String> userIds = channelMap.keySet();
        for (String userId : userIds) {
            Set<Channel> channels = channelMap.get(userId);
            for (Channel channel : channels) {
                if (channel == meChannel) { // 排除当前通道
                    continue;
                }
                channel.writeAndFlush(createTextFrame(data));
            }
        }
    }

    public void pushToUser(Object data, Integer userId) {
        Set<Channel> channels = channelMap.get(String.valueOf(userId));
        if (channels == null) {
            return;
        }
        for (Channel channel : channels) {
            channel.writeAndFlush(createTextFrame(data));
        }
    }

    private TextWebSocketFrame createTextFrame(Object data) {
        MsgModel respModel = new MsgModel(type.name(), data);
        String respText = JsonUtils.objectToJson(respModel);
        return new TextWebSocketFrame(respText);
    }
}
