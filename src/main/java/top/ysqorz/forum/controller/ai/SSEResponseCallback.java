package top.ysqorz.forum.controller.ai;

import java.util.List;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/2/5
 */
public interface SSEResponseCallback<T> {
    void onLineRead(String str, T data);

    void onCompletedRead(String str, List<T> dataList);

    T convertLine(String str);
}
