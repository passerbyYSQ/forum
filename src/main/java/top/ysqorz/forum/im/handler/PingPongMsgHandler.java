package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.MsgCenter;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;

import java.util.Objects;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:07
 */
public class PingPongMsgHandler extends AbstractMsgHandler<MsgModel> {
    private final MsgCenter msgCenter;

    public PingPongMsgHandler(MsgCenter msgCenter) {
        super(MsgType.PING);
        this.msgCenter = msgCenter;
    }

    @Override
    public boolean support(MsgModel msg, Channel channel) {
        if (!super.matchMsgType(msg)) {
            return false;
        }
        ChannelMap channelMap = msgCenter.getChannelMap(msg.getChannelType());
        return (Objects.nonNull(channelMap) && channelMap.isBound(channel));
    }

    @Override
    protected boolean doHandle(MsgModel msg, MsgModel data, Channel channel) {
        // 必须在TailHandler之前消费PING消息，否则PING消息会流指TailHandler导致channel被关闭
        channel.writeAndFlush(IMUtils.createTextFrame(MsgType.PONG));
        return true;
    }

    @Override
    protected boolean doPush(MsgModel data, String sourceChannelId) {
        return false;
    }

    @Override
    public Class<MsgModel> getDataClass() {
        return MsgModel.class;
    }
}
