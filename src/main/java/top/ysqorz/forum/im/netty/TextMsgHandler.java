package top.ysqorz.forum.im.netty;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.im.handler.MsgCenterImpl;
import top.ysqorz.forum.utils.JsonUtils;

/**
 * 用于处理文本消息的handler
 *
 * @author passerbyYSQ
 * @create 2021-02-05 21:23
 */
@Slf4j
public class TextMsgHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        JsonNode msgNode = JsonUtils.jsonToNode(frame.text());
        if (msgNode == null || !msgNode.has("msgType") || !msgNode.has("channelType")) {
            return;
        }
        String msgType = msgNode.get("msgType").asText();
        String channelType = msgNode.get("channelType").asText();
        MsgModel msg = new MsgModel(MsgType.valueOf(msgType), ChannelType.valueOf(channelType), msgNode.get("data"));
        MsgCenterImpl.getInstance().handle(msg, ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        MsgCenterImpl.getInstance().unBind(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Channel error", cause);
        ctx.close();
    }

}
