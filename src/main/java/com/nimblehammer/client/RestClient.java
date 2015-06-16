package com.nimblehammer.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class RestClient {
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private final HttpEntity<String> entity = new HttpEntity<>("", httpHeaders);
    private final Map<String, String> params = new HashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    public void setHeader(String key, String value) {
        httpHeaders.set(key, value);
    }

    public void setParameter(String key, String value) {
        params.put(key, value);
    }

    public <T> ResponseEntity<T> get(String uri, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.GET, entity, responseType, params);
    }
}
