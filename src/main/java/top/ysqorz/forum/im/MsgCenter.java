package top.ysqorz.forum.im;

import top.ysqorz.forum.im.handler.BindMsgHandler;

/**
 * @author passerbyYSQ
 * @create 2025-02-01 22:24
 */
public interface MsgCenter extends MsgOperator, BindMsgHandler.BindEventCallback {
    /**
     * 添加消息处理器
     */
    MsgCenter addHandler(MsgHandler<?> handler);
}
