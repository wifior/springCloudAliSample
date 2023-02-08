package com.soopen.config;

import com.soopen.interceptor.FeignAuthReuestInterceptor;
import feign.Logger;
import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 全局配置添加@Configuration
 * 局部配置不要加@Configuration,在feign接口上添加configuration=FeignConfig.class
 */
@Configuration
public class FeignConfig {

    @Bean
    public Request.Options options(){
        return new Request.Options(5L, TimeUnit.SECONDS, 10L,TimeUnit.SECONDS,true);
    }

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    /**
     * 自定义拦截器
     * @return
     */
    @Bean
    public FeignAuthReuestInterceptor feignAuthReuestInterceptor(){
        return new FeignAuthReuestInterceptor();
    }
}
