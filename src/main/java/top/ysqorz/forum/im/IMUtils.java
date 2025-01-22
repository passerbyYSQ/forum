package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.util.AttributeKey;
import lombok.Getter;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.JsonUtils;

import javax.annotation.PostConstruct;
import java.security.NoSuchAlgorithmException;

/**
 * @author passerbyYSQ
 * @create 2022-01-25 19:12
 */
@Component("IMUtils")
public class IMUtils {
    public static final AttributeKey<String> TOKEN_KEY = AttributeKey.valueOf("token");
    public static final AttributeKey<String> GROUP_ID_KEY = AttributeKey.valueOf("groupId");
    public static final AttributeKey<String> CHANNEL_TYPE_KEY = AttributeKey.valueOf("channelType");
    public static final AttributeKey<Integer> ALL_IDLE_KEY = AttributeKey.valueOf(IdleState.ALL_IDLE.name());

    @Getter
    private static int WebPort;
    @Getter
    private static String WebContextPath;
    @Getter
    private static int WebSocketPort;
    private Environment environment;

    @Autowired
    private void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    private void init() {
        IMUtils.WebPort = environment.getProperty("server.port", int.class, -1);
        IMUtils.WebContextPath = environment.getProperty("server.servlet.context-path", String.class, "");
        IMUtils.WebSocketPort = environment.getProperty("web-socket.port", int.class, -1);
        if (WebPort < 0 || WebSocketPort < 0) {
            throw new RuntimeException("Failed to get service port configuration");
        }
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
