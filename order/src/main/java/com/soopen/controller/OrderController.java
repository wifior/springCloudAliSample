package com.soopen.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.soopen.feign.ProductFeignService;
import com.soopen.feign.StockFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${server.port}")
    String port;

    @Autowired
    StockFeignService stockFeignService;

    @Autowired
    ProductFeignService productFeignService;

    @RequestMapping("get")
    public String getOrder() {
        String msg = stockFeignService.getStock();
        System.out.println("调用库存"+msg);
        System.out.println("查询订单");
        return "查询订单[feign]"+port+"========"+msg;
    }

    @RequestMapping("add")
    public String addOrder(){
        System.out.println("下单中。。。");
        productFeignService.getById(1);

        stockFeignService.reduceStock();

        System.out.println("下单成功！");
        return "下单成功";
    }

    @RequestMapping("/flow")
    @SentinelResource(value = "flow",blockHandler = "flowBlockHandler")
    public String flow(){
        return "正常访问";
    }

    public String flowBlockHandler(BlockException e){
        return "自定义流控信息";
    }
}
