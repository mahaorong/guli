package com.atguigu.gmall.search.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @RequestMapping("/search")
    @ResponseBody
   public String search(){
        System.out.println("修改");
       return"搜索成功";
   }
    public static void main(String[] args) {
        System.out.println("搜索成功");
    }
}
