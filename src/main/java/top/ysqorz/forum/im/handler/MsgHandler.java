package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.Data;
import okhttp3.Call;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.*;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.OkHttpUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author passerbyYSQ
 * @create 2022-01-11 23:57
 */
@Data
public abstract class MsgHandler<DataType> {
    // 能够处理的消息类型
    private MsgType msgType;
    // 通道类型
    private ChannelType channelType;
    // 下一个消息处理器
    private MsgHandler next;
    private boolean isNeedLoginUserInfo;
    // 该类型消息的所有通道。子类使用
    protected ChannelMap channelMap;
    // 线程池异步消费数据库操作。子类使用
    protected ThreadPoolExecutor dbExecutor;

    public MsgHandler(MsgType msgType) {
        this(msgType, null);
    }

    public MsgHandler(MsgType msgType, ChannelType channelType) {
        this(msgType, channelType, false);
    }

    public MsgHandler(MsgType msgType, ChannelType channelType, boolean isNeedLoginUserInfo) {
        this.msgType = msgType;
        this.channelType = channelType;
        this.isNeedLoginUserInfo = isNeedLoginUserInfo;
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
            DataType data = transformData(msg);
            if (data == null) {
                return;
            }
            doPush(data, sourceChannelId);
        } else if (next != null) {
            next.push(msg, sourceChannelId);
        }
    }

    public void remoteDispatch(MsgModel msg, String sourceChannelId, User user) {
        if (checkMsgType(msg)) {
            DataType data = transformData(msg);
            if (data == null) {
                return;
            }
            data = processData(data, user);
            if (data == null) {
                return;
            }
            Set<String> servers = queryServersChannelLocated(data);
            if (servers == null) {
                return;
            }
            // 保存到数据库
            doSave(data);
            msg.setData(data); // completed data
            IMService imService = SpringUtils.getBean(IMService.class);
            Set<String> imServers = new HashSet<>(imService.getIMServerIpList()); // web servers
            // 分发到各个服务器，然后进行推送
            for (String server : servers) {
                if (imServers.contains(server)) { // 如果服务是正常，才转发
                    String api = String.format("http://%s/im/push", server); // 如果设置了context-path，此处需要改动
                    OkHttpUtils.builder().url(api)
                            .addHeader("token", ShiroUtils.getToken())
                            .addParam("msgJson", JsonUtils.objectToJson(msg))
                            .addParam("channelId", sourceChannelId)
                            .post(false).async(new PushApiCallback(data));
                }
            }
        } else if (next != null) {
            next.remoteDispatch(msg, sourceChannelId, user);
        }
    }

    public void addBehind(MsgHandler handler) {
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
        User loginUser = null;
        if (isNeedLoginUserInfo) {
            // Integer userId = ShiroUtils.getUserId();
            Integer userId = IMUtils.getUserIdFromChannel(channel);
            if (userId == null) { // 没有绑定时发送消息
                JsonNode dataNode = msg.transformToDataNode(); // PING消息dataNode为null但又能执行到此处
                if (dataNode != null && dataNode.has("token")) {
                    String token = dataNode.get("token").asText();
                    userId = Integer.valueOf(JwtUtils.getClaimByKey(token, "userId"));
                }
            }
            loginUser = getUserById(userId);
        }
        DataType data = transformData(msg);
        if (data != null) {
            data = processData(data, loginUser);
        }
        return data != null && doHandle0(data, channel, loginUser);
    }

    private User getUserById(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        return userService.getUserById(userId);
    }

    // data is completed
    protected void doSave(DataType data) {
        AsyncInsertTask insertTask = createAsyncInsertTask(data);
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

    protected abstract DataType transformData(MsgModel msg); // must implement

    protected DataType processData(DataType data, User user) {
        return data; // 默认不处理直接返回
    }

    protected AsyncInsertTask createAsyncInsertTask(DataType data) {
        return null;
    }

    /**
     * 消费数据
     *
     * @return true：消费完成，不继续往下投递；false：未消费完成，继续往下投递
     */
    protected abstract boolean doHandle0(DataType data, Channel channel, User loginUser);

    class PushApiCallback implements OkHttpUtils.ApiCallback {
        DataType data;

        PushApiCallback(DataType data) {
            this.data = data;
        }

        @Override
        public void onSucceed(Call call, String bodyJson) {
            // do nothing
        }

        @Override
        public void onFailed(Call call, String errorMsg) {
            // TODO retry
        }
    }
}
