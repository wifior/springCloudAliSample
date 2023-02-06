package com.soopen.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Value("${server.port}")
    String port;

    @RequestMapping("get")
    public String getOrder() {
        System.out.println("查询订单");
        return "查询订单"+port;
    }
}
