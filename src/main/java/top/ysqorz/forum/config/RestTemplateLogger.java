package top.ysqorz.forum.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.utils.CommonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RestTemplateLogger implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.debug("URI          : {}", request.getURI());
        log.debug("Method       : {}", request.getMethod());
        log.debug("Header       : {}", request.getHeaders());
        log.debug("Request body : {}", new String(body, StandardCharsets.UTF_8));
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        StringBuilder sbd = new StringBuilder();
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
        String line = bufReader.readLine();
        while (line != null) {
            sbd.append(line);
            sbd.append('\n');
            line = bufReader.readLine();
        }
        log.debug("Status code  : {}", response.getStatusCode());
        log.debug("Header       : {}", response.getHeaders());
        log.debug("Response body: {}", sbd);
    }

}