package top.ysqorz.forum.im.entity;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.Data;
import top.ysqorz.forum.im.IMUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author passerbyYSQ
 * @create 2022-01-12 0:00
 */
@Data
public class ChannelMap {
    // 通道的业务类型。如：DANMU, CHAT ... etc
    private MsgType type; // channelType
    // 该业务类型的所有通道。groupId(videoId, userId... etc) --> Set<Channel>
    private Map<String, Set<Channel>> channelMap = new ConcurrentHashMap<>();
    private volatile AtomicInteger channelCount = new AtomicInteger(0);

    public ChannelMap(MsgType type) {
        this.type = type;
    }

    public void bind(Integer userId, String groupId, Channel channel) {
        Set<Channel> channels = channelMap.get(groupId);
        // 往channel里面存储额外信息，实现双向绑定，以便能通过能够移除Map中的channel
        AttributeKey<Integer> userIdKey = AttributeKey.valueOf("userId");
        channel.attr(userIdKey).set(userId);
        AttributeKey<String> groupIdKey = AttributeKey.valueOf("groupId");
        channel.attr(groupIdKey).set(groupId);
        AttributeKey<String> channelTypeKey = AttributeKey.valueOf("channelType");
        channel.attr(channelTypeKey).set(type.name());
        if (channels == null) {
            Set<Channel> newChannels = new HashSet<>();
            newChannels.add(channel);
            channelMap.put(groupId, newChannels);
        } else {
            channels.add(channel);
        }
        channelCount.incrementAndGet();
    }

    public void unBind(Channel channel) {
        String groupId = IMUtils.getGroupIdFromChannel(channel);
        if (groupId != null) {
            Set<Channel> channels = channelMap.get(groupId);
            if (channels != null) {
                channels.remove(channel);
                channelCount.decrementAndGet();
            }
        }
    }

    public void pushToGroup(Object data, Channel sourceChannel, String groupId) {
        Set<Channel> channels = channelMap.get(groupId);
        if (channels == null) {
            return;
        }
        for (Channel channel : channels) {
            if (channel == sourceChannel) {
                continue;
            }
            channel.writeAndFlush(createTextFrame(data));
        }
    }

    public boolean isBound(Channel channel) {
        Integer userId = IMUtils.getUserIdFromChannel(channel);
        String channelType = IMUtils.getChannelTypeFromChannel(channel);
        String groupId = IMUtils.getGroupIdFromChannel(channel);
        if (userId == null || channelType == null || groupId == null) {
            return false;
        }
        Set<Channel> channels = channelMap.get(groupId);
        return channelType.equals(type.name()) && (channels != null && channels.contains(channel));
    }

    public Channel findChannel(String groupId, String channelId) {
        Set<Channel> channels = channelMap.get(groupId);
        if (channels == null) {
            return null;
        }
        for (Channel channel : channels) {
            if (channel.id().asLongText().equals(channelId)) {
                return channel;
            }
        }
        return null;
    }

    private TextWebSocketFrame createTextFrame(Object data) {
        return IMUtils.createTextFrame(type, data);
    }

}
