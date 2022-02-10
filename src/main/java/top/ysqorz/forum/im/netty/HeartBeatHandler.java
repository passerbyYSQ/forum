package top.ysqorz.forum.im.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.MsgType;

/**
 * 之所以不继承SimpleChannelInboundHandler方法，而实现它的父类ChannelInboundHandlerAdapter
 * 是因为我们的心跳保持不需要实现channelRead0()这个方法
 *
 * @author passerbyYSQ
 * @create 2021-02-08 22:11
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) { // 空闲状态
            IdleStateEvent event = (IdleStateEvent) evt;
            Channel channel = ctx.channel();
            String channelId = channel.id().asLongText();

            if (event.state() == IdleState.READER_IDLE) {
                log.info("channel进入[读]空闲状态：{}", channelId);
            } else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("channel进入[写]空闲状态：{}", channelId);
                channel.writeAndFlush(IMUtils.createTextFrame(MsgType.PONG));
                resetChannelAllIdleCount(channel);
            } else if (event.state() == IdleState.ALL_IDLE) {
                log.info("channel进入[读写]空闲状态：{}", channelId);
                Integer allIdleCount = channel.attr(IMUtils.ALL_IDLE_KEY).get();
                if (Integer.valueOf(3).equals(allIdleCount)) { // >=3
                    log.info("channel[读写]空闲状态超过3次，已关闭：{}", channelId);
                    channel.writeAndFlush(IMUtils.createTextFrame(MsgType.CLOSE));
                    channel.close();
                } else { // <3
                    channel.attr(IMUtils.ALL_IDLE_KEY).compareAndSet(allIdleCount, allIdleCount + 1);
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 一旦有消息可读，包括但不限于PING消息，都打破了空闲状态的嫌疑，重置allIdleCount为0
        resetChannelAllIdleCount(ctx.channel());
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        resetChannelAllIdleCount(ctx.channel());
    }

    public static void resetChannelAllIdleCount(Channel channel) {
        channel.attr(IMUtils.ALL_IDLE_KEY).set(0);
    }
}
