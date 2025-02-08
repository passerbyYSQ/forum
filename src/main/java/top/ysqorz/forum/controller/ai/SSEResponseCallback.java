package top.ysqorz.forum.controller.ai;

import java.io.IOException;
import java.util.List;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/2/5
 */
public interface SSEResponseCallback<T> {
    void onLineRead(String str, T data) throws IOException;

    void onCompletedRead(String str, List<T> dataList) throws IOException;

    T convertLine(String str);
}
