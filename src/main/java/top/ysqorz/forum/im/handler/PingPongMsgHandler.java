package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:07
 */
public class PingPongMsgHandler extends AbstractMsgHandler<MsgModel> {

    public PingPongMsgHandler() {
        super(MsgType.PING);
    }

    @Override
    protected boolean doHandle(MsgModel msg, MsgModel data, Channel channel, String token) {
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
