package com.soopen.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "product-service",path = "/product")
public interface ProductFeignService {

    @RequestMapping("getById/{id}")
    public String getById(@PathVariable("id") Integer id);

}
