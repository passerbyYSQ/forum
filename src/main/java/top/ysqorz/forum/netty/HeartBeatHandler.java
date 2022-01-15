package top.ysqorz.forum.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 之所以不继承SimpleChannelInboundHandler方法，而实现它的父类ChannelInboundHandlerAdapter
 * 是因为我们的心跳保持不需要实现channelRead0()这个方法
 *
 * @author passerbyYSQ
 * @create 2021-02-08 22:11
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) { // 空闲状态
            IdleStateEvent event = (IdleStateEvent) evt;
            Channel channel = ctx.channel();
            String channelId = channel.id().asLongText();

            if (event.state() == IdleState.READER_IDLE) {
//                logger.info("channel进入读空闲状态：{}", channelId);

            } else if (event.state() == IdleState.WRITER_IDLE) {
//                logger.info("channel进入写空闲状态：{}", channelId);

            } else if (event.state() == IdleState.ALL_IDLE) {
//                logger.info("channel进入写读写空闲状态：{}，剩余通道个数：{}",
//                        channelId, UserChannelRepository.CHANNEL_GROUP.size());
//                UserChannelRepository.print();
            }
        }
    }


}
