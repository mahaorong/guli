package com.atguigu.gmall.search.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @RequestMapping("/search")
    @ResponseBody
    public String search() {
        System.out.println("修改");
        System.out.println("已经更新到1.0版本");
        System.out.println("小吴修改版的代码--------");
        System.out.println("小肖修改了代码");
        return "搜索成功";

    }
}

