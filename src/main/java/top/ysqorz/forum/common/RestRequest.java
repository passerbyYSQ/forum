package top.ysqorz.forum.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2022-11-08 22:46
 */
public class RestRequest {
    private String url;
    private Map<String, String> header;
    private Map<String, String> params;
    private Object postBody;

    private RestRequest() {

    }

    public static RestRequest builder() {
        return new RestRequest();
    }

    public RestRequest url(String url) {
        this.url = url;
        return this;
    }

    public RestRequest addParams(String key, String value) {
        if (params == null) {
            params = new LinkedHashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public RestRequest addHeader(String key, String value) {
        if (header == null) {
            header = new LinkedHashMap<>();
        }
        header.put(key, value);
        return this;
    }

    public <T> T get(Class<T> clazz) {
        Map<String, String> encodedParams = encodeUrlParams(params);
        String completedUrl = generateUrl(url, encodedParams.keySet());
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(header);
        ResponseEntity<T> response = restTemplate.exchange(completedUrl, HttpMethod.GET, entity, clazz, encodedParams);
        return response.getBody();
    }

    private Map<String, String> encodeUrlParams(Map<String, String> params) {
        Map<String, String> encodedParams = new HashMap<>();
        params.forEach((key, value) -> {
            String encodedKey = CommonUtils.urlEncode(key);
            String encodedValue = CommonUtils.urlEncode(value);
            encodedParams.put(encodedKey, encodedValue);
        });
        return encodedParams;
    }

    private String generateUrl(String rawUrl, Set<String> keys) {
        StringBuilder completedUrl = new StringBuilder(rawUrl);
        StringBuilder paramStr = new StringBuilder();
        for (String key : keys) {
            paramStr.append(key).append("=").append("?"); // 占位符
        }
        int questionIdx = completedUrl.lastIndexOf("?");
        int equalIdx = completedUrl.lastIndexOf("=");
        if (paramStr.length() > 0) {
            if (questionIdx == -1) { // 没有问号
                completedUrl.append("?");
            } else if (equalIdx != -1 && questionIdx < equalIdx) { // 有问号和等号，且问号在等号之前
                completedUrl.append("&");
            }
            completedUrl.append(paramStr);
        }
        return completedUrl.toString();
    }
}
