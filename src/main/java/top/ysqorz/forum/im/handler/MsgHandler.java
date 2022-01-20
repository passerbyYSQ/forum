package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.Data;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author passerbyYSQ
 * @create 2022-01-11 23:57
 */
@Data
public abstract class MsgHandler {
    // 能够处理的消息类型
    private MsgType type;
    // 下一个消息处理器
    private MsgHandler next;
    // 该类型消息的所有通道。子类使用
    protected ChannelMap channelMap;
    // 线程池异步消费数据库操作。子类使用
    protected ThreadPoolExecutor dbExecutor;

    public MsgHandler(ThreadPoolExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    public MsgHandler(MsgType type) {
        this(type, null);
    }

    public MsgHandler(MsgType type, ThreadPoolExecutor dbExecutor) {
        this.type = type;
        this.dbExecutor = dbExecutor;
    }

    public void handle(MsgModel msg, Channel channel) {
        // 不能处理 或者 能处理但未处理完
        // next != null 必须放到后面，否则会短路导致doHandle不被执行
        if ((!canHandle(msg, channel) || !doHandle(msg, channel)) && next != null) {
            next.handle(msg, channel);
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
        return type != null && type.name().equalsIgnoreCase(msg.getType()) && isLogin(msg, channel);
    }

    protected final boolean isLogin(MsgModel msg, Channel channel) {
        return checkBound(channel) || checkToken(msg); // checkLoginByShiro() ||
    }

    protected final boolean checkLoginByShiro() {
        return ShiroUtils.isAuthenticated();
    }

    protected final boolean checkBound(Channel channel) {
        return channelMap != null && channelMap.isBound(channel); // BIND和TAIL的channelMap为空
    }

    protected final boolean checkToken(MsgModel msg) {
        JsonNode dataNode = msg.transformToDataNode();
        if (dataNode == null || !dataNode.has("token")) {
            return false;
        }
        String token = dataNode.get("token").asText();
        String userId = JwtUtils.getClaimByKey(token, "userId");
        User user = getUserById(userId);
        return user != null && JwtUtils.verifyJwt(token, user.getJwtSalt());
    }

    private boolean doHandle(MsgModel msg, Channel channel) {
        // String userId = String.valueOf(ShiroUtils.getUserId());
        AttributeKey<String> userIdKey = AttributeKey.valueOf("userId");
        String userId = channel.attr(userIdKey).get();
        if (userId == null) { // 没有绑定时发送消息
            JsonNode dataNode = msg.transformToDataNode(); // PING消息dataNode为null但又能执行到此处
            if (dataNode != null && dataNode.has("token")) {
                String token = dataNode.get("token").asText();
                userId = JwtUtils.getClaimByKey(token, "userId");
            }
        }
        return doHandle0(msg, channel, getUserById(userId));
    }

    private User getUserById(String userId) {
        if (userId == null) {
            return null;
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        return userService.getUserById(Integer.valueOf(userId));
    }

    /**
     * 消费数据
     *
     * @return true：消费完成，不继续往下投递；false：未消费完成，继续往下投递
     */
    protected abstract boolean doHandle0(MsgModel msg, Channel channel, User loginUser);

}
