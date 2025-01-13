package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.util.AttributeKey;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.JsonUtils;

import java.security.NoSuchAlgorithmException;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:12
 */
@Component
public class IMUtils {
    public static final AttributeKey<String> TOKEN_KEY = AttributeKey.valueOf("token");
    public static final AttributeKey<String> GROUP_ID_KEY = AttributeKey.valueOf("groupId");
    public static final AttributeKey<String> CHANNEL_TYPE_KEY = AttributeKey.valueOf("channelType");
    public static final AttributeKey<Integer> ALL_IDLE_KEY = AttributeKey.valueOf(IdleState.ALL_IDLE.name());

    public static int WebPort;
    public static String WebContextPath;
    public static int WebSocketPort;

    @Value("${server.port}")
    private void setWebPort(int webPort) {
        IMUtils.WebPort = webPort;
    }

    @Value("${server.servlet.context-path:}")
    private void setWebContextPath(String webContextPath) {
        IMUtils.WebContextPath = webContextPath;
    }

    @Value("${web-socket.port}")
    private void setWebSocketPort(int webSocketPort) {
        IMUtils.WebSocketPort = webSocketPort;
    }

    public static String getWebServer() {
        return CommonUtils.getLocalHostStr() + ":" + WebPort;
    }

    public static String getWebSocketServer() {
        return String.format("ws://%s:%d/ws", CommonUtils.getLocalHostStr(), WebSocketPort);
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
        String respText = JsonUtils.objToJson(respModel);
        return new TextWebSocketFrame(respText);
    }

    public static TextWebSocketFrame createTextFrame(MsgType type) {
        return createTextFrame(type, null);
    }

    public static String getTokenFromChannel(Channel channel) {
        return channel.attr(TOKEN_KEY).get();
    }

    public static String getChannelTypeFromChannel(Channel channel) {
        return channel.attr(CHANNEL_TYPE_KEY).get();
    }

    public static String getGroupIdFromChannel(Channel channel) {
        return channel.attr(GROUP_ID_KEY).get();
    }
}
