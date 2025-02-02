package top.ysqorz.forum.im;

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

    MsgHandlerPipeline addHandlerAtIndex(int index, MsgHandler<?> handler);

    MsgHandler<?> getHeadHandler();

    MsgHandler<?> getTailHandler();

    MsgHandler<?> getHandler(int index);

    void removeHandler(MsgHandler<?> handler);

    void removeHandler(int index);

    boolean exist(MsgHandler<?> handler);

    boolean isEmpty();

    int size();
}
