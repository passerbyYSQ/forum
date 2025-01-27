package top.ysqorz.forum.im.handler;

import java.util.Iterator;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/1/27
 */
public interface MsgHandlerPipeline extends Iterable<MsgHandler<?>>, Iterator<MsgHandler<?>> {
    default MsgHandlerPipeline addHandler(MsgHandler<?> handler) {
        return addHandlerAtTail(handler);
    }

    MsgHandlerPipeline addHandlerAtHead(MsgHandler<?> handler);

    MsgHandlerPipeline addHandlerAtTail(MsgHandler<?> handler);

    MsgHandlerPipeline addHandlerBefore(MsgHandler<?> baseHandler, MsgHandler<?> newHandler);

    MsgHandlerPipeline addHandlerAfter(MsgHandler<?> baseHandler, MsgHandler<?> newHandler);

    MsgHandler<?> findPrevHandler(MsgHandler<?> handler);

    boolean exist(MsgHandler<?> handler);

    MsgHandler<?> getHeadHandler();

    MsgHandler<?> getTailHandler();

    void removeHandler(MsgHandler<?> handler);

    boolean isEmpty();
}
