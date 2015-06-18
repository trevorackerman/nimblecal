package com.nimblehammer.client;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RestClient {
    private final Map<String, String> httpHeaders = new HashMap<>();
    private final Map<String, String> params = new HashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    public void setHeader(String key, String value) {
        httpHeaders.put(key, value);
    }

    public void setParameter(String key, String value) {
        params.put(key, value);
    }

    public <T> T get(String uri, Class<T> responseType) {
        restTemplate.setInterceptors(Arrays.asList(new HeadersHttpRequestInterceptor(httpHeaders)));
        return restTemplate.getForObject(uri, responseType, params);
    }


}
