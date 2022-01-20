package top.ysqorz.forum.im.entity;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.Data;
import top.ysqorz.forum.utils.JsonUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
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

    public void bind(String userId, Channel channel, Object extra) {
        Set<Channel> channels = channelMap.get(userId);
        // 往channel里面存储额外信息，实现双向绑定，以便能通过能够移除Map中的channel
        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");
        channel.attr(userIdKey).set(userId);
        AttributeKey<String> channelTypeKey = AttributeKey.valueOf("channelType");
        channel.attr(channelTypeKey).set(type.name());
        AttributeKey<Object> extraKey = AttributeKey.valueOf("extra");
        channel.attr(extraKey).set(extra);
        if (channels == null) {
            Set<Channel> newChannels = new HashSet<>();
            newChannels.add(channel);
            channelMap.put(userId, newChannels);
        } else {
            channels.add(channel);
        }
    }

    public void unBind(Channel channel) {
        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");
        String userId = channel.attr(userIdKey).get();
        if (userId != null) {
            Set<Channel> channels = channelMap.get(userId);
            if (channels != null) {
                channels.remove(channel);
            }
        }
    }

    public void pushExceptCurr(Object data, Channel currChannel) {
        pushExceptCurr(data, currChannel, null);
    }

    public void pushExceptCurr(Object data, Channel currChannel, ChannelMatcher matcher) {
        Set<String> userIds = channelMap.keySet();
        for (String userId : userIds) {
            Set<Channel> channels = channelMap.get(userId);
            for (Channel channel : channels) {
                if (channel == currChannel) { // 排除当前通道
                    continue;
                }
                AttributeKey<Object> extraKey = AttributeKey.valueOf("extra");
                Object extra = channel.attr(extraKey).get();
                if (matcher == null || matcher.isMatch(extra, channel)) {
                    channel.writeAndFlush(createTextFrame(data));
                }
            }
        }
    }

    public void pushToUser(Object data, Integer userId) {
        pushToUser(data, userId, null);
    }

    public void pushToUser(Object data, Integer userId, ChannelMatcher matcher) {
        Set<Channel> channels = channelMap.get(String.valueOf(userId));
        if (channels == null) {
            return;
        }
        for (Channel channel : channels) {
            AttributeKey<Object> extraKey = AttributeKey.valueOf("extra");
            Object extra = channel.attr(extraKey).get();
            if (matcher == null || matcher.isMatch(extra, channel)) {
                channel.writeAndFlush(createTextFrame(data));
            }
        }
    }

    public boolean isBound(Channel channel) {
        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");
        String userId = channel.attr(userIdKey).get();
        if (userId == null) {
            return false;
        }
        Set<Channel> channels = channelMap.get(userId);
        if (channels == null) {
            return false;
        }
        return channels.contains(channel);
    }

    private TextWebSocketFrame createTextFrame(Object data) {
        MsgModel respModel = new MsgModel(type, data);
        String respText = JsonUtils.objectToJson(respModel);
        return new TextWebSocketFrame(respText);
    }

    public interface ChannelMatcher {
        boolean isMatch(Object extra, Channel channel);
    }
}
