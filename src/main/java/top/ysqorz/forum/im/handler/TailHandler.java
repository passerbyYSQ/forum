package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;

/**
 * 位于尾部的后置处理器
 *
 * @author passerbyYSQ
 * @create 2022-01-18 19:29
 */
public class TailHandler extends MsgHandler<MsgModel> {

    public TailHandler() {
        super(null);
    }

    @Override
    protected boolean canHandle(MsgModel msg, Channel channel) {
        return true; // 可以处理所有流到尾部、未被消费完的消息
    }

    @Override
    protected MsgModel transformData(MsgModel msg) {
        return msg; // must return msg, or doHandle0 do not invoke
    }

    @Override
    protected boolean doHandle0(MsgModel msg, Channel channel, User loginUser) {
        // 消息能流至最后一个处理器，只能说客户端建立了长连接后，没有进行绑定操作，就发送消息，从而导致未被消费
        // 此时认为长连接非法建立，强制关闭长连接
        channel.writeAndFlush(IMUtils.createTextFrame(MsgType.CLOSE));
//        MsgCenter.getInstance().unBind(channel);
        channel.close(); // 这里close会回调到 TextMsgHandler 中的 channelInactive。所以此处不需要 unbind
        return true;
    }
}
