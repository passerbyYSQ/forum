package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.ChannelType;

public interface BindEventCallback {
    void bind(ChannelType channelType, String token, String groupId, Channel channel);

    void unBind(Channel channel);
}
