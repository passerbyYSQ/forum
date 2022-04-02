package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.AsyncInsertTask;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgType;

import java.util.Set;

/**
 * 针对业务类型的消息处理，加强规范
 *
 * @author passerbyYSQ
 * @create 2022-02-11 20:52
 */
public abstract class NonFunctionalMsgHandler<DataType> extends MsgHandler<DataType> {
    /**
     * 加强规范。业务类型的消息指明msgType和channelType
     */
    public NonFunctionalMsgHandler(MsgType msgType, ChannelType channelType) {
        super(msgType, channelType);
    }

    /**
     * 针对业务类型的消息，doHandle0单机版时有用，分布式版无用。
     * 分布式版需要使用remoteDispatch和push两个方法，不使用handle方法
     */
    @Override
    protected boolean doHandle0(DataType data, Channel channel) {
        this.doSave(data);
        this.doPush(data, channel.id().asLongText());
        return true;
    }

    /**
     * 加强规范。分布式下转发消息需要从Redis查询出目标服务，必须实现此方法
     */
    @Override
    protected abstract Set<String> queryServersChannelLocated(DataType data);

    /**
     * 加强规范。业务类型的消息由于需要推送，必须实现此方法
     */
    @Override
    protected abstract void doPush(DataType data, String sourceChannelId);

    /**
     * 加强规范。业务类型的消息需要存储到数据库，必须实现此方法
     */
    @Override
    protected abstract AsyncInsertTask createAsyncInsertTask(DataType data);


}
