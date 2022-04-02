package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:07
 */
public class PingPongMsgHandler extends MsgHandler<MsgModel> {

    public PingPongMsgHandler() {
        super(MsgType.PING);
    }

    @Override
    protected boolean isLogin(MsgModel msg, Channel channel) {
        ChannelMap channelMap = MsgCenter.getInstance().getChannelMap(msg.getChannelType());
        return (channelMap != null && channelMap.isBound(channel)) || super.checkToken(msg);
    }

    @Override
    protected boolean doHandle0(MsgModel msg, Channel channel) {
        // 必须在TailHandler之前消费PING消息，否则PING消息会流指TailHandler导致channel被关闭
        channel.writeAndFlush(IMUtils.createTextFrame(MsgType.PONG));
        return true;
    }
}
