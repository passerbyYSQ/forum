package top.ysqorz.forum.im.utils;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.utils.JsonUtils;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:12
 */
public class IMUtils {
    public static TextWebSocketFrame createTextFrame(MsgType type, Object data) {
        MsgModel respModel = new MsgModel(type, data);
        String respText = JsonUtils.objectToJson(respModel);
        return new TextWebSocketFrame(respText);
    }

    public static TextWebSocketFrame createTextFrame(MsgType type) {
        return createTextFrame(type, null);
    }

    public static String getUserIdFromChannel(Channel channel) {
        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");
        return channel.attr(userIdKey).get();
    }

    public static String getChannelTypeFromChannel(Channel channel) {
        AttributeKey<String> channelTypeKey = AttributeKey.valueOf("channelType");
        return channel.attr(channelTypeKey).get();
    }

    public static Object getExtraFromChannel(Channel channel) {
        AttributeKey<Object> extraKey = AttributeKey.valueOf("extra");
        return channel.attr(extraKey).get();
    }
}
