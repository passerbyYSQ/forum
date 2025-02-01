package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.MsgModel;

/**
 * @author passerbyYSQ
 * @create 2025-02-01 22:54
 */
public interface MsgOperator {
    /**
     * 处理消息
     */
    boolean handle(MsgModel msg, Channel channel);

    /**
     * 在当前服务节点，推送消息到客户端
     */
    boolean push(MsgModel msg, String channelId);
}
