package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.AllArgsConstructor;
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

    public MsgHandler(MsgType type) {
        this.type = type;
    }

    public void handle(MsgModel msg, Channel channel) {
        CheckResult checkRes = canHandle(msg);
        if (checkRes.isGoOn && !doHandle(msg, channel, checkRes.user)) {
            next.handle(msg, channel);
        }
    }

    // 子类可重写
    protected CheckResult canHandle(MsgModel msg) {
        CheckResult checkRes = new CheckResult(false);
        if (type.name().equalsIgnoreCase(msg.getType())) {
            checkRes = checkLogin(msg);
        }
        return checkRes;
    }

    // 可以让子类调用，但不能重写
    protected final CheckResult checkLogin(MsgModel msg) {
        if (!type.isNeedLogin) {
            return new CheckResult(true); // 不需要登录直接放行
        }
        // 需要登录
        CheckResult checkRes = checkLoginByShiro(); // 优先通过shiro判断是否已经登录
        if (!checkRes.isGoOn) { // 再通过检查token
            checkRes = checkToken(msg);
        }
        return checkRes;
    }

    // 可以让子类调用，但不能重写
    protected final CheckResult checkToken(MsgModel msg) {
        JsonNode dataNode = msg.getDataNode();
        if (dataNode == null || !dataNode.has("token")) {
            return new CheckResult(false);
        }
        String token = dataNode.get("token").asText();
        String userId = JwtUtils.getClaimByKey(token, "userId"); // TODO 待抽取常量
        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.getUserById(Integer.valueOf(userId));
        boolean isValidToken = JwtUtils.verifyJwt(token, user.getJwtSalt());
        return new CheckResult(isValidToken, user);
    }

    protected final CheckResult checkLoginByShiro() {
        if (!ShiroUtils.isAuthenticated()) {
            return new CheckResult(false);
        }
        UserService userService = SpringUtils.getBean(UserService.class);
        User user = userService.getUserById(ShiroUtils.getUserId());
        return new CheckResult(true, user);
    }

    /**
     * 消费数据
     *
     * @return true：消费完成，不继续往下投递；false：未消费完成，继续往下投递
     */
    protected abstract boolean doHandle(MsgModel msg, Channel channel, User loginUser);

    @AllArgsConstructor
    static class CheckResult {
        Boolean isGoOn;
        User user;

        CheckResult(Boolean isGoOn) {
            this.isGoOn = isGoOn;
        }
    }
}
