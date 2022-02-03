package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.Data;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.Set;
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
    private boolean isNeedLoginUserInfo;
    // 该类型消息的所有通道。子类使用
    protected ChannelMap channelMap;
    // 线程池异步消费数据库操作。子类使用
    protected ThreadPoolExecutor dbExecutor;

    public MsgHandler(ThreadPoolExecutor dbExecutor) {
        this.dbExecutor = dbExecutor;
    }

    public MsgHandler(MsgType type) {
        this(type, null, false);
    }

    public MsgHandler(MsgType type, ThreadPoolExecutor dbExecutor, boolean isNeedLoginUserInfo) {
        this.type = type;
        this.dbExecutor = dbExecutor;
        this.isNeedLoginUserInfo = isNeedLoginUserInfo;
    }

    public void handle(MsgModel msg, Channel channel) {
        // 不能处理 或者 能处理但未处理完
        // next != null 必须放到后面，否则会短路导致doHandle不被执行
        if ((!canHandle(msg, channel) || !doHandle(msg, channel)) && next != null) {
            next.handle(msg, channel);
        }
    }

    // 返回最终要插入到数据库的PO
    public Object save(MsgModel msg, Integer userId) {
        if (checkMsgType(msg)) { // 当前handler消费
            return doSave(msg, userId);
        } else if (next != null) { // 如果当前handler不能消费，且下一个handler不为空，则交由下一个handler消费
            return next.save(msg, userId);
        }
        return null;
    }

    public void remoteDispatch(MsgModel msg) {
        if (checkMsgType(msg)) {
            Set<String> servers = queryServersChannelLocated(msg);
            if (servers == null) {
                return;
            }
            for (String server : servers) {
                // TODO remote invoke push api
            }
        } else if (next != null) {
            next.remoteDispatch(msg);
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
        return type != null && type.name().equalsIgnoreCase(msg.getMsgType());
    }

    protected boolean isLogin(MsgModel msg, Channel channel) {
        return checkBound(channel) || checkToken(msg); // checkLoginByShiro() ||
    }

    protected boolean checkLoginByShiro() {
        return ShiroUtils.isAuthenticated();
    }

    protected boolean checkBound(Channel channel) {
        return channelMap != null && channelMap.isBound(channel); // BIND和TAIL的channelMap为空
    }

    protected final boolean checkToken(MsgModel msg) {
        JsonNode dataNode = msg.transformToDataNode();
        if (dataNode == null || !dataNode.has("token")) {
            return false;
        }
        String token = dataNode.get("token").asText();
        String userId = JwtUtils.getClaimByKey(token, "userId");
        User user = getUserById(Integer.valueOf(userId));
        return user != null && JwtUtils.verifyJwt(token, user.getJwtSalt());
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
        return doHandle0(msg, channel, loginUser);
    }

    private User getUserById(Integer userId) {
        if (userId == null) {
            return null;
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        return userService.getUserById(userId);
    }

    /**
     * 如果业务类型的消息需要存到数据库，则子类需要重写此方法
     */
    protected Object doSave(MsgModel msg, Integer userId) {
        return null;
    }

    protected Set<String> queryServersChannelLocated(MsgModel msg) {
        return null;
    }

    /**
     * 消费数据
     *
     * @return true：消费完成，不继续往下投递；false：未消费完成，继续往下投递
     */
    protected abstract boolean doHandle0(MsgModel msg, Channel channel, User loginUser);
}
