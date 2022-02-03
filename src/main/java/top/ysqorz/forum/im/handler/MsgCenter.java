package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.MsgModel;
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
    private Set<String> addedHandlers = new HashSet<>(); // 已经添加的外置handlers
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

    public synchronized MsgCenter addLast(MsgHandler handler) { // 链式调用
        if (handler == null || handler.getType() == null) {
            return instance;
        }
        String type = handler.getType().name();
        if (addedHandlers.contains(type)) {
            return instance;
        }
        addedHandlers.add(type);
        ChannelMap channelMap = new ChannelMap(handler.getType());
        typeToChannels.put(type, channelMap);
        handler.setChannelMap(channelMap);
        handler.setDbExecutor(dbExecutor);

        tail.addBehind(handler);
        tail = handler;

        return instance;
    }

    public void bind(String channelType, Integer userId, String groupId, Channel channel) {
        ChannelMap channelMap = typeToChannels.get(channelType);
        if (channelMap != null) {
            channelMap.bind(userId, groupId, channel);
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            redisService.bindWsServer(channelType, groupId);
            int count = channelCount.incrementAndGet();
            log.info("绑定成功，当前通道数：{}", count);
        }
    }

    public void unBind(Channel channel) {
        String channelType = IMUtils.getChannelTypeFromChannel(channel);
        if (channelType != null) {
            ChannelMap channelMap = typeToChannels.get(channelType);
            if (channelMap != null) {
                channelMap.unBind(channel);
                RedisService redisService = SpringUtils.getBean(RedisService.class);
                String groupId = IMUtils.getGroupIdFromChannel(channel);
                redisService.removeWsServer(channelType, groupId);
                int count = channelCount.decrementAndGet();
                log.info("解绑成功，当前通道数：{}", count);
            }
        }
    }

    public ChannelMap getChannelMap(String channelType) {
        return typeToChannels.get(channelType);
    }

    private void initInternalHandlers() {
        UserChannelBindHandler bindHandler = new UserChannelBindHandler(dbExecutor);
        // 需要增加 PingPongMsgHandler，否则已经登录情况下Ping消息会流至TailHandler，导致通道关闭
        PingPongMsgHandler pingPongHandler = new PingPongMsgHandler();
        TailHandler tailHandler = new TailHandler(dbExecutor);

        addedHandlers.add(bindHandler.getType().name());
        addedHandlers.add(pingPongHandler.getType().name());

        bindHandler.addBehind(pingPongHandler);
        pingPongHandler.addBehind(tailHandler);

        first = bindHandler;
        tail = pingPongHandler; // 不包括tailHandler
    }

    public void handle(MsgModel msg, Channel channel) {
        first.handle(msg, channel);
    }

    public void save(MsgModel msg, Integer userId) {
        first.save(msg, userId);
    }

    public Channel findChannel(String channelType, String groupId, String channelId) {
        ChannelMap channelMap = this.getChannelMap(channelType);
        if (channelMap == null) {
            return null;
        }
        return channelMap.findChannel(groupId, channelId);
    }
}
