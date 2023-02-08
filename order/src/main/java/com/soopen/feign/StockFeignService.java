package com.soopen.feign;

import com.soopen.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * name rest接口所在的服务名
 * path 所在的controller
 */
@FeignClient(name = "stock-service",path = "/stock",configuration= FeignConfig.class,fallback = StockFeignServiceFallback.class)
public interface StockFeignService {

    //与rest接口对应的方法
    @RequestMapping("get")
    String getStock();

    @RequestMapping("reduce")
    String reduceStock();

    /*@RequestMapping("get")
    public String getStock() {
        System.out.println("查询库存");
        return "查询库存"+port;
    }*/
}
