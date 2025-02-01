package top.ysqorz.forum.im.handler;

import top.ysqorz.forum.im.MsgHandler;
import top.ysqorz.forum.im.MsgHandlerPipeline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/1/27
 */
public class LinkedListMsgHandlerPipeline implements MsgHandlerPipeline {
    private final LinkedList<MsgHandler<?>> list = new LinkedList<>();
    private Iterator<MsgHandler<?>> iterator;

    protected void checkNotNull(MsgHandler<?> handler) {
        if (Objects.isNull(handler)) {
            throw new NullPointerException();
        }
    }

    @Override
    public MsgHandlerPipeline addHandlerAtHead(MsgHandler<?> handler) {
        checkNotNull(handler);
        list.addFirst(handler);
        return this;
    }

    @Override
    public MsgHandlerPipeline addHandlerAtTail(MsgHandler<?> handler) {
        checkNotNull(handler);
        list.addLast(handler);
        return this;
    }

    @Override
    public MsgHandlerPipeline addHandlerAtIndex(int index, MsgHandler<?> handler) {
        checkNotNull(handler);
        list.add(index, handler);
        return this;
    }

    @Override
    public MsgHandler<?> getHeadHandler() {
        if (isEmpty()) {
            return null;
        }
        return list.getFirst();
    }

    @Override
    public MsgHandler<?> getTailHandler() {
        if (isEmpty()) {
            return null;
        }
        return list.getLast();
    }

    @Override
    public MsgHandler<?> getHandler(int index) {
        return list.get(index);
    }

    @Override
    public void removeHandler(MsgHandler<?> handler) {
        list.remove(handler);
    }

    @Override
    public void removeHandler(int index) {
        list.remove(index);
    }

    @Override
    public boolean exist(MsgHandler<?> handler) {
        return list.contains(handler);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Iterator<MsgHandler<?>> iterator() {
        iterator = list.iterator();
        return this;
    }

    @Override
    public boolean hasNext() {
        return Objects.nonNull(iterator) && iterator.hasNext();
    }

    @Override
    public void remove() {
        if (Objects.isNull(iterator)) {
            return;
        }
        iterator.remove();
    }

    @Override
    public MsgHandler<?> next() {
        if (Objects.isNull(iterator)) {
            return null;
        }
        return iterator.next();
    }
}
