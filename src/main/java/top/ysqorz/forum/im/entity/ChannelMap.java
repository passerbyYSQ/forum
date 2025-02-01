package top.ysqorz.forum.im.entity;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.handler.FakeChannel;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
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
    private ChannelType channelType;
    // 该业务类型的所有通道。groupId(videoId, userId... etc) --> Set<Channel>
    private Map<String, Set<Channel>> channelMap = new ConcurrentHashMap<>();
    private volatile AtomicInteger channelCount = new AtomicInteger(0);

    public ChannelMap(ChannelType channelType) {
        this.channelType = channelType;
    }

    public void bind(String token, String groupId, Channel channel) {
        // 往channel里面存储额外信息，实现双向绑定，以便能通过能够移除Map中的channel
        channel.attr(IMUtils.TOKEN_KEY).set(token);
        channel.attr(IMUtils.GROUP_ID_KEY).set(groupId);
        channel.attr(IMUtils.CHANNEL_TYPE_KEY).set(channelType.name());
        // 双重检测锁
        Set<Channel> channels;
        if (Objects.isNull(channels = channelMap.get(groupId))) {
            synchronized (this) {
                if (Objects.isNull(channels = channelMap.get(groupId))) {
                    channels = new HashSet<>();
                    channelMap.put(groupId, channels);
                }
            }
        }
        channels.add(channel);
        channelCount.incrementAndGet();
    }

    public void unBind(Channel channel) {
        String groupId = IMUtils.getGroupIdFromChannel(channel);
        if (groupId != null) {
            Set<Channel> channels = channelMap.get(groupId);
            if (channels != null) {
                channels.remove(channel);
                if (channels.size() == 0) {
                    channelMap.remove(groupId);
                }
                channelCount.decrementAndGet();
            }
        }
    }

    public void pushToGroup(MsgType msgType, Object data, String sourceChannelId, String groupId) {
        Set<Channel> channels = channelMap.get(groupId);
        if (channels == null) {
            return;
        }
        for (Channel channel : channels) {
            if (channel.id().asLongText().equals(sourceChannelId)) {
                continue;
            }
            TextWebSocketFrame textFrame = IMUtils.createTextFrame(msgType, data);
            channel.writeAndFlush(textFrame);
        }
    }

    public boolean isBound(Channel channel) {
        String token = IMUtils.getTokenFromChannel(channel);
        if (channel instanceof FakeChannel && Objects.nonNull(token)) {
            return true;
        }
        String channelType = IMUtils.getChannelTypeFromChannel(channel);
        String groupId = IMUtils.getGroupIdFromChannel(channel);
        if (token == null || channelType == null || groupId == null) {
            return false;
        }
        Set<Channel> channels = channelMap.get(groupId);
        return channelType.equals(this.channelType.name()) && (channels != null && channels.contains(channel));
    }
}
