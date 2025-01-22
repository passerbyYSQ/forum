package top.ysqorz.forum.im;

import cn.hutool.core.util.ObjectUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.util.AttributeKey;
import lombok.Getter;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.DependsOn;
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
@DependsOn("server-org.springframework.boot.autoconfigure.web.ServerProperties")
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

    @Autowired
    private IMUtils(ServerProperties serverProperties) {
        IMUtils.WebPort = serverProperties.getPort();
        IMUtils.WebContextPath = ObjectUtil.defaultIfEmpty(serverProperties.getServlet().getContextPath(), "");
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
