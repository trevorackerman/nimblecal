package com.nimblehammer.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.util.Map;

class HeadersHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private final Map<String, String> headers;

    public HeadersHttpRequestInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
        requestWrapper.getHeaders().setAll(headers);

        return execution.execute(requestWrapper, body);
    }


}
