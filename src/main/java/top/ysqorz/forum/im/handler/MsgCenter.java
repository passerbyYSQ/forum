package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author passerbyYSQ
 * @create 2022-01-12 0:25
 */
public class MsgCenter {
    private LinkedList<MsgHandler> handlerChain = new LinkedList<>();
    private Set<String> addedHandlers = new HashSet<>();
    private Map<String, ChannelMap> typeToChannels = new ConcurrentHashMap<>(); // channelType --> ChannelMap
    private ThreadPoolExecutor dbExecutor;

    private static MsgCenter instance = new MsgCenter();

    private MsgCenter() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        dbExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize, 10,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000));
        handlerChain.addLast(new UserChannelBindHandler());
    }

    public static MsgCenter getInstance() {
        return instance;
    }

    public synchronized MsgCenter addLast(MsgHandler handler) {
        // 处理BIND类型的消息的handler已经被内置添加到责任链的第一个了，不允许外部添加
        if (handler == null || handler.getType() == null || MsgType.BIND.equals(handler.getType())) {
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

        MsgHandler last = handlerChain.getLast();
        if (last != null) {
            last.setNext(handler);
        }
        handlerChain.addLast(handler);
        return instance;
    }

    public void bind(String channelType, String userId, Channel channel) {
        ChannelMap channelMap = typeToChannels.get(channelType);
        if (channelMap != null) {
            channelMap.bind(userId, channel);
        }
    }

    public void handle(MsgModel msg, Channel channel) {
        handlerChain.getFirst().handle(msg, channel);
    }

}
