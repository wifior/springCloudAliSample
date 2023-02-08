package com.soopen.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.UUID;

public class FeignAuthReuestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        String access_token = UUID.randomUUID().toString();
        System.out.println("Authorization"+access_token);
        template.header("Authorization",access_token);
    }
}
