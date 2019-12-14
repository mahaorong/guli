package com.atguigu.gmall.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrderController {

    @RequestMapping("index")
    @ResponseBody
    public String index() {
        return "index";
    }


    public static void main(String[] args) {
        System.out.println("我是小彭，我负责订单模块");
    }
}
