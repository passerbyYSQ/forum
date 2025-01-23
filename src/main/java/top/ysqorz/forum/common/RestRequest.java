package top.ysqorz.forum.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2022-11-08 22:46
 */
@Slf4j
public class RestRequest {
    private String url;
    private MultiValueMap<String, String> header;
    private Map<String, String> params;
    private Object requestBody;
    private MultiValueMap<String, Object> formData;
    private final RestTemplate restTemplate;

    private RestRequest() {
        restTemplate = SpringUtils.getBean(RestTemplate.class);
        header = new LinkedMultiValueMap<>();
        params = new LinkedHashMap<>();
        formData = new LinkedMultiValueMap<>();
    }

    public static RestRequest builder() {
        return new RestRequest();
    }

    public RestRequest url(String url) {
        this.url = url;
        return this;
    }

    public RestRequest body(Object body) {
        this.requestBody = body;
        return this;
    }

    public RestRequest addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public RestRequest addHeader(String key, String value) {
        header.add(key, value);
        return this;
    }

    public RestRequest addFormData(String key, Object value) {
        formData.add(key, value);
        return this;
    }

    public <T> T get(Class<T> clazz) {
        String completedUrl = generateUrl(url, params);
        ResponseEntity<T> response = restTemplate.exchange(completedUrl, HttpMethod.GET, new HttpEntity<>(header), clazz, params);
        return response.getBody();
    }

    public <T> T post(Class<T> clazz) {
        String completedUrl = generateUrl(url, params);
        Object body = ObjectUtils.isEmpty(requestBody) ? formData : requestBody;
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<T> response = restTemplate.exchange(completedUrl, HttpMethod.POST, httpEntity, clazz, params);
        if (!HttpStatus.OK.equals(response.getStatusCode())) {
            log.error("HTTP request failed: {}", response);
        }
        return response.getBody();
    }

    private String generateUrl(String rawUrl, Map<String, String> params) {
        if (ObjectUtils.isEmpty(params)) {
            return rawUrl;
        }
        StringBuilder completedUrl = new StringBuilder(rawUrl);
        StringBuilder paramStr = new StringBuilder();
        for (String key : params.keySet()) {
            paramStr.append(key).append("=").append(String.format("{%s}", key)).append("&"); // 占位符
        }
        paramStr.deleteCharAt(paramStr.length() - 1);
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
