package top.ysqorz.forum.controller.ai;

import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SSEResponseExtractor<T> implements ResponseExtractor<List<T>> {
    public final String DATA_PREFIX = "data:";
    private final SSEResponseCallback<T> callback;

    public SSEResponseExtractor(SSEResponseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    public List<T> extractData(ClientHttpResponse response) throws IOException {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RestClientException("Non-2xx response");
        }
        MediaType contentType = response.getHeaders().getContentType();
//        if (Objects.isNull(contentType) || contentType.includes(MediaType.TEXT_EVENT_STREAM)) {
//            throw new RestClientException("Invalid content type: " + contentType);
//        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
            String line;
            StringBuilder totalStr = new StringBuilder();
            List<T> dataList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith(DATA_PREFIX)) {
                    String str = line.substring(DATA_PREFIX.length()).trim();
                    T data = callback.convertLine(str);
                    callback.onLineRead(str, data);
                    totalStr.append(str).append("\n");
                    dataList.add(data);
                }
                // 可扩展处理其他字段（如 event:、id:、retry:）
            }
            callback.onCompletedRead(totalStr.toString(), dataList);
            return dataList;
        }
    }
}