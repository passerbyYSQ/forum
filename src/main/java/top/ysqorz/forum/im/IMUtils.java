package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.core.env.Environment;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.utils.IpUtils;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:12
 */
public class IMUtils {

    public static String getWsServer() {
        return String.format("ws://%s:%d/ws", IpUtils.getLocalIp(), Constant.WS_SERVER_PORT);
    }

    public static String getWebServer() {
        Environment env = SpringUtils.getBean(Environment.class);
        return IpUtils.getLocalIp() + ":" + env.getProperty("server.port");
    }

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

    public static Integer getUserIdFromChannel(Channel channel) {
        AttributeKey<Integer> userIdKey = AttributeKey.valueOf("userId");
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
