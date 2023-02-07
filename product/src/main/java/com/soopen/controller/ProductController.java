package com.soopen.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {


    @RequestMapping("getById/{id}")
    public String getById(@PathVariable("id") Integer id){
        return "查询到商品"+id;
    }
}
