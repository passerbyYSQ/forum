package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.utils.JsonUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:12
 */
public class IMUtils {

    public static String generateAuthDigest(String userPwd) {
        // user:passowrd  --sha1--> --base64--> digest
        try {
            return DigestAuthenticationProvider.generateDigest(userPwd);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    public static String getGroupIdFromChannel(Channel channel) {
        AttributeKey<String> channelTypeKey = AttributeKey.valueOf("groupId");
        return channel.attr(channelTypeKey).get();
    }
}
