package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.MsgModel;

/**
 * 位于尾部的后置处理器
 *
 * @author passerbyYSQ
 * @create 2022-01-18 19:29
 */
public class TailHandler extends AbstractMsgHandler<MsgModel> {

    public TailHandler() {
        super(null);
    }

    @Override
    public boolean support(MsgModel msg, Channel channel) {
        return true; // 可以处理所有流到尾部、未被消费完的消息
    }

    @Override
    public Class<MsgModel> getDataClass() {
        return MsgModel.class;
    }

    @Override
    protected boolean doHandle(MsgModel msg, MsgModel data, Channel channel) {
        // 消息能流至最后一个处理器，只能说客户端建立了长连接后，没有进行绑定操作，就发送消息，从而导致未被消费
        // 此时认为长连接非法建立，强制关闭长连接
        // MsgCenter.getInstance().unBind(channel);
        channel.close(); // 这里close会回调到 TextMsgHandler 中的 channelInactive。所以此处不需要 unbind
        return true;
    }

    @Override
    protected boolean doPush(MsgModel data, String sourceChannelId) {
        return false;
    }
}
