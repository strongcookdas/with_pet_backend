package com.ajou_nice.with_pet.utils;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

@Component
public class HttpUtil {
    public HttpHeaders getHeaders(MultiValueMap<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.addAll(headers);
        return httpHeaders;
    }

    public HttpEntity<Map<String,String>> getHttpEntity(MultiValueMap<String, String> headers, Map<String,String> parameters){
        return new HttpEntity<>(parameters, this.getHeaders(headers));
    }
}
