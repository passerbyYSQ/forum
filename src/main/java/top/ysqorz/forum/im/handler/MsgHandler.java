package top.ysqorz.forum.im.handler;

import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpHeaders;
import top.ysqorz.forum.common.RestRequest;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.*;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author passerbyYSQ
 * @create 2022-01-11 23:57
 */
@Getter
@Slf4j
public abstract class MsgHandler<DataType> {
    // 能够处理的消息类型
    private final MsgType msgType;
    // 通道类型
    private final ChannelType channelType;
    // 下一个消息处理器
    @Setter
    private MsgHandler<?> next;
    // 该类型消息的所有通道。子类使用
    protected ChannelMap channelMap;
    // 线程池异步消费数据库操作。子类使用
    protected ThreadPoolExecutor dbExecutor;

    public MsgHandler(MsgType msgType) {
        this(msgType, null);
    }

    public MsgHandler(MsgType msgType, ChannelType channelType) {
        this.msgType = msgType;
        this.channelType = channelType;
    }

    public void handle(MsgModel msg, Channel channel) {
        // 不能处理 或者 能处理但未处理完的需要交给下一个handler处理
        // next != null 必须放到后面，否则会短路导致doHandle不被执行
        if ((!canHandle(msg, channel) || !doHandle(msg, channel)) && next != null) {
            next.handle(msg, channel);
        }
    }

    public void push(MsgModel msg, String sourceChannelId) { // msg.data is completed
        if (checkMsgType(msg)) {
            DataType data = transformData(msg); // data is DataType
            if (data == null) {
                return;
            }
            doPush(data, sourceChannelId);
        } else if (next != null) {
            next.push(msg, sourceChannelId);
        }
    }

    public void remoteDispatch(MsgModel msg, String sourceChannelId, String token) {
        if (checkMsgType(msg)) {
            DataType data = transformData(msg); // data is DataType
            if (data == null) {
                return;
            }
            // 保存到数据库
            doSave(data);
            msg.setData(data); // completed data
            doRemoteDispatch(msg, sourceChannelId, token);
        } else if (next != null) {
            next.remoteDispatch(msg, sourceChannelId, token);
        }
    }

    protected void doRemoteDispatch(MsgModel msg, String sourceChannelId, String token) {
        DataType data = (DataType) msg.getData();
        Set<String> servers = queryServersChannelLocated(data);
        if (servers == null) {
            return;
        }
        IMService imService = SpringUtils.getBean(IMService.class);
        Set<String> imServers = new HashSet<>(imService.getIMServerIpList()); // web servers
        // 分发到各个服务器，然后进行推送
        //log.info("Msg: {}", JsonUtils.objToJson(msg));
        //log.info("Channel Located Servers: {}", servers);
        //log.info("IM Servers: {}", imServers);
        for (String server : servers) {
            if (imServers.contains(server)) { // 如果服务是正常，才转发
                String api = String.format("http://%s/im/push%s", server, IMUtils.getWebContextPath());
                RestRequest restRequest = RestRequest.builder().url(api)
                        .addHeader(HttpHeaders.AUTHORIZATION, token)
                        .body(msg);
                if (sourceChannelId != null) {
                    restRequest.addParam("channelId", sourceChannelId);
                }
                restRequest.post(JSONObject.class);
            }
        }
    }

    public void addBehind(MsgHandler<?> handler) {
        if (handler == null) {
            return;
        }
        handler.next = next;
        next = handler;
    }

    // 子类可重写
    protected boolean canHandle(MsgModel msg, Channel channel) {
        return checkMsgType(msg) && isLogin(msg, channel);
    }

    protected boolean checkMsgType(MsgModel msg) {
        return msgType != null && msgType.name().equalsIgnoreCase(msg.getMsgType());
    }

    protected boolean isLogin(MsgModel msg, Channel channel) {
        return checkBound(channel) || checkToken(msg);
    }

    protected boolean checkBound(Channel channel) {
        return channelMap != null && channelMap.isBound(channel); // 功能类型的消息，channelMap为空
    }

    protected final boolean checkToken(MsgModel msg) {
        JsonNode dataNode = msg.transformToDataNode();
        if (dataNode == null || !dataNode.has("token")) {
            return false;
        }
        String token = dataNode.get("token").asText();
        String userId = JwtUtils.getClaimByKey(token, "userId");
        User user = getUserById(Integer.valueOf(userId));
        return user != null && JwtUtils.verifyJwt(token, user.getLoginSalt());
    }

    private boolean doHandle(MsgModel msg, Channel channel) {
        channel.attr(IMUtils.ALL_IDLE_KEY).set(0);  // 有合法消息可消费，重置空闲次数
        DataType data = transformData(msg); // data is JsonNode
        if (data != null) {
            return doHandle0(data, channel);
        }
        return false;
    }

    // data is completed
    protected void doSave(DataType data) {
        AsyncInsertTask<DataType> insertTask = createAsyncInsertTask(data);
        if (insertTask == null) {
            return;
        }
        this.dbExecutor.execute(insertTask);
    }

    // data is completed
    protected void doPush(DataType data, String sourceChannelId) {
    }

    protected Set<String> queryServersChannelLocated(DataType data) {
        return null;
    }

    protected AsyncInsertTask<DataType> createAsyncInsertTask(DataType data) {
        return null;
    }

    private User getUserById(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        return userService.getUserById(userId);
    }

    private DataType transformData(MsgModel msg) {
        ResolvableType resolvableType = ResolvableType.forClass(this.getClass());
        ResolvableType[] types = resolvableType.getSuperType().getGenerics();
        Class<DataType> clazz = (Class<DataType>) types[0].resolve(); // 只有一个泛型，且需是参数类型
        if (MsgModel.class.equals(clazz)) {
            return clazz.cast(msg); // 如果需要整个MsgModel直接返回
        }
        // 转换data部分
        // 由于远程转发，序列化成Object对象后实际类型是LinkedHashMap，这个时候强转类型报错
        // java.lang.ClassCastException: Cannot cast java.util.LinkedHashMap to top.ysqorz.forum.po.DanmuMsg
        // clazz.cast(msg.getData());
        // 解决方法：LinkedHashMap => JsonNode => 指定类型
        return JsonUtils.nodeToObj(msg.transformToDataNode(), clazz);
    }

    /**
     * 消费数据
     *
     * @return true：消费完成，不继续往下投递；false：未消费完成，继续往下投递
     */
    protected abstract boolean doHandle0(DataType data, Channel channel);

}
