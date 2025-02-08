package top.ysqorz.forum.controller.ai;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 各个AI接口的响应提取器
 */
public class SSEResponseExtractor<T> implements ResponseExtractor<List<T>> {
    private final String dataPrefix;
    private final String doneFlag;
    private final SSEResponseCallback<T> callback;

    public SSEResponseExtractor(String dataPrefix, String doneFlag, SSEResponseCallback<T> callback) {
        this.dataPrefix = dataPrefix;
        this.doneFlag = doneFlag;
        this.callback = callback;
    }

    @Override
    public List<T> extractData(ClientHttpResponse response) throws IOException {
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RestClientException("Non-2xx response");
//        }
        MediaType contentType = response.getHeaders().getContentType();
//        if (Objects.isNull(contentType) || contentType.includes(MediaType.TEXT_EVENT_STREAM)) {
//            throw new RestClientException("Invalid content type: " + contentType);
//        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
            String line;
            StringBuilder totalStr = new StringBuilder();
            List<T> dataList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                // 收集全部字符
                totalStr.append(line).append("\n");

                // 解析数据行
                if (line.startsWith(dataPrefix)) {
                    String str = line.substring(dataPrefix.length()).trim();
                    if (doneFlag.equalsIgnoreCase(str)) {
                        break;
                    }
                    T data = callback.convertLine(str);
                    callback.onLineRead(line, data);
                    dataList.add(data);
                }
                // 可扩展处理其他字段（如 event:、id:、retry:）
            }
            callback.onCompletedRead(totalStr.toString(), dataList);
            return dataList;
        }
    }
}