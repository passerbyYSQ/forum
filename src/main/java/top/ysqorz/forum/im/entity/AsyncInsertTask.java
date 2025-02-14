package top.ysqorz.forum.im.entity;

import top.ysqorz.forum.common.BaseMapper;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 1:03
 */
public class AsyncInsertTask<T> implements Runnable { // T：PO的类型
    private final BaseMapper<T> mapper;
    private final T data;
    private final int retryCount;

    public AsyncInsertTask(BaseMapper<T> mapper, T data) {
        this(mapper, data, 3);
    }

    public AsyncInsertTask(BaseMapper<T> mapper, T data, int retryCount) {
        this.mapper = mapper;
        this.data = data;
        this.retryCount = retryCount;
    }

    @Override
    public void run() {
        if (mapper == null) {
            return;
        }
        for (int i = 0; i < retryCount; i++) {
            int affectedRows = mapper.insert(data);
            if (affectedRows == 1) { // 成功立马跳出
                break;
            }
            // 有可能时数据库连接不够，导致插入失败
            // 线程池中的该线程持锁睡眠100后重试。其他要推送的消息会堆放到阻塞队列中
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
