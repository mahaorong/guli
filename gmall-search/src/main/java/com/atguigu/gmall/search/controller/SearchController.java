package com.atguigu.gmall.search.controller;


<<<<<<<<< Temporary merge branch 1
public class SearchController {
    public static void main(String[] args) {
        System.out.println("搜索成功");
    }
=========
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchController {
    @RequestMapping("/search")
    @ResponseBody
   public String search(){
        System.out.println("修改");
        System.out.println("1.0");
        System.out.println("2.0");
       return"搜索成功";
   }
}
