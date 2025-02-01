package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;

import java.util.concurrent.Executor;


/**
 * @author passerbyYSQ
 * @create 2025-02-01 22:32
 */
public interface MsgHandler<DataType> extends MsgOperator {
    /**
     * 当前处理器是否支持处理此消息
     */
    boolean support(MsgModel msg, Channel channel);

    /**
     * 消息的POJO类
     */
    Class<DataType> getDataClass();

    /**
     * 当前消息处理器支持的消息类型
     */
    MsgType getMsgType();

    /**
     * 当前消息处理器支持的通道类型
     */
    ChannelType getChannelType();

    /**
     * 设置异步线程池
     */
    void setExecutor(Executor executor);

    void setChannelMap(ChannelMap channelMap);
}
