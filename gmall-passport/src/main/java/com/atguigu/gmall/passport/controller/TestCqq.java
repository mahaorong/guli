package com.atguigu.gmall.passport.controller;

import com.atguigu.gmall.util.HttpclientUtil;

import java.util.HashMap;
import java.util.Map;

public class TestCqq {
    //用授权码交换access_token地址
    public static void main(String[] args) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("client_id", "2421967659");
        paramMap.put("client_secret", "d3c06562a544a109be6a4317c970182c");
        paramMap.put("grant_type", "authorization_code");
        paramMap.put("redirect_uri", "http://passport.gmall.com:8086/vlogin");
        paramMap.put("code", "c6cd51b91eafbfa0271b284aa74529ab");
        String result = HttpclientUtil.doPost("https://api.weibo.com/oauth2/access_token", paramMap);
        System.out.println(result);
    }
}
