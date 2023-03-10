package com.soopen.controller;
import com.soopen.feign.OrderFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Value("${server.port}")
    String port;

    @RequestMapping("get")
    public String getStock() {
        System.out.println("查询库存");
        return "查询库存[feign]"+port+"========";
    }

    @RequestMapping("reduce")
    public String reduceStock(){
        System.out.println("减库存[feign]");
        return "减库存[feign]"+port;

    }
}
