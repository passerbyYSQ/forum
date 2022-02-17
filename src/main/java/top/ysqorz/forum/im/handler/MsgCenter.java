package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author passerbyYSQ
 * @create 2022-01-12 0:25
 */
@Slf4j
public class MsgCenter {
    private MsgHandler first, tail; // handler链
    private Set<String> addedMsgTypes = new HashSet<>();
    private Map<String, ChannelMap> typeToChannels = new ConcurrentHashMap<>(); // channelType --> ChannelMap
    private volatile AtomicInteger channelCount = new AtomicInteger(0);
    private ThreadPoolExecutor dbExecutor;

    private static MsgCenter instance = new MsgCenter();

    private MsgCenter() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        dbExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000));
        initInternalHandlers();
    }

    public static MsgCenter getInstance() {
        return instance;
    }

    public synchronized MsgCenter addHandlerAtLast(MsgHandler handler) { // 链式调用
        if (handler == null || handler.getMsgType() == null || handler.getChannelType() == null) {
            return instance;
        }
        String msgType = handler.getMsgType().name();
        if (addedMsgTypes.contains(msgType)) {
            return instance;
        }
        addedMsgTypes.add(msgType);

        String channelType = handler.getChannelType().name();
        ChannelMap channelMap = typeToChannels.get(channelType);
        // 如果该channelType的channelMap不存在则创建，否则使用已创建的。因为可能多个handler共用一个channelMap
        if (channelMap == null) {
            channelMap = new ChannelMap(handler.getMsgType(), handler.getChannelType());
            typeToChannels.put(channelType, channelMap);
        }

        handler.setChannelMap(channelMap);
        handler.setDbExecutor(dbExecutor);

        tail.addBehind(handler);
        tail = handler;

        return instance;
    }

    public void bind(ChannelType channelType, Integer userId, String groupId, Channel channel) {
        ChannelMap channelMap = typeToChannels.get(channelType.name());
        if (channelMap != null) {
            channelMap.bind(userId, groupId, channel);
            int count = channelCount.incrementAndGet();
            log.info("绑定成功，当前通道数：{}", count);
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            redisService.bindWsServer(channelType, groupId);
        }
    }

    public void unBind(Channel channel) {
        String channelType = IMUtils.getChannelTypeFromChannel(channel);
        if (channelType != null) {
            ChannelMap channelMap = typeToChannels.get(channelType);
            if (channelMap != null) {
                channelMap.unBind(channel);
                int count = channelCount.decrementAndGet();
                log.info("解绑成功，当前通道数：{}", count);
                RedisService redisService = SpringUtils.getBean(RedisService.class);
                String groupId = IMUtils.getGroupIdFromChannel(channel);
                redisService.removeWsServer(ChannelType.valueOf(channelType), groupId);
            }
        }
    }

    public void handle(MsgModel msg, Channel channel) {
        first.handle(msg, channel);
    }

    public void push(MsgModel msg, String sourceChannelId) { // source user
        first.push(msg, sourceChannelId);
    }

    public void remoteDispatch(MsgModel msg, String sourceChannelId, User user) { // source user
        first.remoteDispatch(msg, sourceChannelId, user);
    }

    public ChannelMap getChannelMap(String channelType) {
        return typeToChannels.get(channelType);
    }

    private void initInternalHandlers() {
        BindMsgHandler bindHandler = new BindMsgHandler();
        // 需要增加 PingPongMsgHandler，否则已经登录情况下Ping消息会流至TailHandler，导致通道关闭
        PingPongMsgHandler pingPongHandler = new PingPongMsgHandler();
        TailHandler tailHandler = new TailHandler();

        addedMsgTypes.add(bindHandler.getMsgType().name());
        addedMsgTypes.add(pingPongHandler.getMsgType().name());

        bindHandler.addBehind(pingPongHandler);
        pingPongHandler.addBehind(tailHandler);

        first = bindHandler;
        tail = pingPongHandler; // 不包括tailHandler
    }
}
