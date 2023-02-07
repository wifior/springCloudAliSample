package com.soopen.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "order-service",path = "/order")
public interface OrderFeignService {

    @RequestMapping("get")
    String getOrder();
}
