package com.soopen.feign;

import org.springframework.stereotype.Component;

@Component
public class StockFeignServiceFallback implements StockFeignService{
    @Override
    public String getStock() {
        return "降级了！！！";
    }

    @Override
    public String reduceStock() {
        return "降级了！！！";
    }
}
