package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import top.ysqorz.forum.im.*;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.OnOffLineAware;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author passerbyYSQ
 * @create 2022-01-12 0:25
 */
@Slf4j
public class MsgCenterImpl implements MsgCenter {
    private final MsgHandlerPipeline pipeline = new LinkedListMsgHandlerPipeline();
    private final Set<String> addedMsgTypes = new HashSet<>();
    private final Map<String, ChannelMap> typeToChannels = new ConcurrentHashMap<>(); // channelType --> ChannelMap
    private final AtomicInteger channelCount = new AtomicInteger(0);
    private final ThreadPoolExecutor executor;
    // 单例
    @Getter
    private final static MsgCenterImpl instance = new MsgCenterImpl();

    private MsgCenterImpl() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        executor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("MsgCenter-Executor-" + thread.getId());
            return thread;
        });
        initInternalHandlers();
    }

    private void initInternalHandlers() {
        BindMsgHandler bindHandler = new BindMsgHandler(this);
        // 需要增加 PingPongMsgHandler，否则已经登录情况下Ping消息会流至TailHandler，导致通道关闭
        PingPongMsgHandler pingPongHandler = new PingPongMsgHandler();
        TailHandler tailHandler = new TailHandler();

        addedMsgTypes.add(bindHandler.getMsgType().name());
        addedMsgTypes.add(pingPongHandler.getMsgType().name());

        pipeline.addHandler(bindHandler)
                .addHandler(pingPongHandler)
                .addHandler(tailHandler);
    }

    @Override
    public synchronized MsgCenter addHandler(MsgHandler<?> handler) { // 链式调用
        if (handler == null || handler.getMsgType() == null || handler.getChannelType() == null) {
            return instance;
        }
        String msgType = handler.getMsgType().name();
        if (addedMsgTypes.contains(msgType)) {
            return instance;
        }
        addedMsgTypes.add(msgType);

        String channelType = handler.getChannelType().name();
        ChannelMap channelMap = getChannelMap(channelType);
        // 如果该channelType的channelMap不存在则创建，否则使用已创建的。因为可能多个handler共用一个channelMap
        if (channelMap == null) {
            channelMap = new ChannelMap(handler.getChannelType());
            typeToChannels.put(channelType, channelMap);
        }

        handler.setChannelMap(channelMap);
        handler.setExecutor(executor);

        // 最后一个是TailHandler，所有的业务Handler应该在此之前
        pipeline.addHandlerAtIndex(pipeline.size() - 1, handler);
        return instance;
    }

    private void invokeOnOffLineAware(ChannelType channelType, Channel channel, String event) {
        // BindMsgHandler(first) -> PingPongMsgHandler -> xxxHandler(tail) -> TailHandler
        for (MsgHandler<?> msgHandler : pipeline) {
            if (msgHandler instanceof OnOffLineAware &&
                    msgHandler.getChannelType().equals(channelType)) {
                OnOffLineAware aware = (OnOffLineAware) msgHandler;
                switch (event) {
                    case "online":
                        aware.online(channel);
                        break;
                    case "offline":
                        aware.offline(channel);
                        break;
                }
            }
        }
    }

    @Override
    public boolean handle(MsgModel msg, Channel channel) {
        for (MsgHandler<?> msgHandler : pipeline) {
            if (msgHandler.handle(msg, channel)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean push(MsgModel msg, String sourceChannelId) { // source user
        for (MsgHandler<?> msgHandler : pipeline) {
            if (msgHandler.push(msg, sourceChannelId)) {
                return true;
            }
        }
        return false;
    }

    private ChannelMap getChannelMap(String channelType) {
        return typeToChannels.get(channelType);
    }

    @Override
    public void bind(ChannelType channelType, String token, String groupId, Channel channel) {
        ChannelMap channelMap = getChannelMap(channelType.name());
        if (Objects.isNull(channelMap)) {
            return;
        }
        channelMap.bind(token, groupId, channel);
        int count = channelCount.incrementAndGet();
        log.info("绑定成功，当前通道数：{}", count);
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.bindWsServer(channelType, groupId);
        // 调用实现了OnOffLineAware接口的handler的online()方法
        invokeOnOffLineAware(channelType, channel, "online");
    }

    public void unBind(Channel channel) {
        String channelType = IMUtils.getChannelTypeFromChannel(channel);
        ChannelMap channelMap = getChannelMap(channelType);
        if (Objects.isNull(channelMap)) {
            return;
        }
        channelMap.unBind(channel);
        int count = channelCount.decrementAndGet();
        log.info("解绑成功，当前通道数：{}", count);
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        String groupId = IMUtils.getGroupIdFromChannel(channel);
        redisService.removeWsServer(ChannelType.valueOf(channelType), groupId);
        // 调用实现了OnOffLineAware接口的handler的online()方法
        String token = IMUtils.getTokenFromChannel(channel);
        invokeOnOffLineAware(ChannelType.valueOf(channelType), channel, "offline");
    }

}
