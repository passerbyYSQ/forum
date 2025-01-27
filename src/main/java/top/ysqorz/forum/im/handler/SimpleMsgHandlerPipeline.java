package top.ysqorz.forum.im.handler;

import java.util.Iterator;
import java.util.Objects;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/1/27
 */
public class SimpleMsgHandlerPipeline implements MsgHandlerPipeline {
    private MsgHandler<?> head;
    private MsgHandler<?> tail;
    private MsgHandler<?> curr;

    @Override
    public MsgHandlerPipeline addHandlerAtHead(MsgHandler<?> handler) {
        if (Objects.isNull(handler)) {
            return this;
        }
        if (Objects.isNull(head)) {
            head = tail = handler;
        } else {
            head.addBehind(handler);
            head = handler;
        }
        return this;
    }

    @Override
    public MsgHandlerPipeline addHandlerAtTail(MsgHandler<?> handler) {
        if (Objects.isNull(handler)) {
            return this;
        }
        if (Objects.isNull(tail)) {
            head = tail = handler;
        } else {
            tail.addBehind(handler);
            tail = handler;
        }
        return this;
    }

    @Override
    public MsgHandler<?> findPrevHandler(MsgHandler<?> handler) {
        if (Objects.isNull(handler) || isEmpty()) {
            return null;
        }
        MsgHandler<?> prev = head;
        MsgHandler<?> curr = head.getNext();
        while (Objects.nonNull(curr)) {
            if (Objects.equals(curr, handler)) {
                return prev;
            }
            prev = curr;
            curr = curr.getNext();
        }
        return prev;
    }

    @Override
    public MsgHandlerPipeline addHandlerBefore(MsgHandler<?> baseHandler, MsgHandler<?> newHandler) {
        if (Objects.isNull(baseHandler) || Objects.isNull(newHandler) || isEmpty()) {
            return this;
        }
        MsgHandler<?> prev = findPrevHandler(baseHandler);
        if (Objects.nonNull(prev)) {
            prev.addBehind(newHandler);
        }
        return this;
    }

    @Override
    public boolean exist(MsgHandler<?> handler) {
        if (Objects.isNull(handler) || isEmpty()) {
            return false;
        }
        MsgHandler<?> curr = head;
        while (Objects.nonNull(curr)) {
            if (Objects.equals(curr, handler)) {
                return true;
            }
            curr = curr.getNext();
        }
        return false;
    }

    @Override
    public MsgHandlerPipeline addHandlerAfter(MsgHandler<?> baseHandler, MsgHandler<?> newHandler) {
        if (Objects.isNull(baseHandler) || Objects.isNull(newHandler) || isEmpty()) {
            return this;
        }
        if (exist(baseHandler)) {
            baseHandler.addBehind(newHandler);
        }
        return this;
    }

    @Override
    public MsgHandler<?> getHeadHandler() {
        return head;
    }

    @Override
    public MsgHandler<?> getTailHandler() {
        return tail;
    }

    @Override
    public void removeHandler(MsgHandler<?> handler) {
        if (Objects.isNull(handler) || isEmpty()) {
            return;
        }
        MsgHandler<?> prev = findPrevHandler(handler);
        if (Objects.isNull(prev)) {
            return;
        }
        prev.setNext(handler.getNext());
        handler.setNext(null);
    }

    @Override
    public boolean isEmpty() {
        return Objects.isNull(head) || Objects.isNull(tail);
    }

    @Override
    public Iterator<MsgHandler<?>> iterator() {
        curr = head;
        return this;
    }

    @Override
    public boolean hasNext() {
        return Objects.nonNull(curr);
    }

    @Override
    public MsgHandler<?> next() {
        if (Objects.isNull(curr)) {
            return null;
        }
        MsgHandler<?> next = curr;
        curr = curr.getNext();
        return next;
    }
}
