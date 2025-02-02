package top.ysqorz.forum.im;

import top.ysqorz.forum.im.entity.ChannelMap;
import top.ysqorz.forum.im.handler.BindEventCallback;
import top.ysqorz.forum.im.handler.BindMsgHandler;

/**
 * @author passerbyYSQ
 * @create 2025-02-01 22:24
 */
public interface MsgCenter extends MsgOperator, BindEventCallback {
    /**
     * 添加消息处理器
     */
    MsgCenter addHandler(MsgHandler<?> handler);

    /**
     * 获取对应类型的所有通道
     */
    ChannelMap getChannelMap(String channelType);
}
