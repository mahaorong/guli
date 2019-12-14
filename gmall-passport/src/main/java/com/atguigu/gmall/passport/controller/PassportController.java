package com.atguigu.gmall.passport.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UserService;
import com.atguigu.gmall.util.HttpclientUtil;
import com.atguigu.gmall.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {
    @Reference
    UserService userService;

    @RequestMapping("vlogin")
    public String vlogin(String code, HttpServletRequest request) {
        //用code换取access_token
        String url = "https://api.weibo.com/oauth2/access_token";//?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE\n;
        Map<String, String> tokenmap = new HashMap<>();
        tokenmap.put("client_id", "2421967659");
        tokenmap.put("client_secret", "d3c06562a544a109be6a4317c970182c");
        tokenmap.put("grant_type", "authorization_code");
        tokenmap.put("redirect_uri", "http://passport.gmall.com:8086/vlogin");
        tokenmap.put("code", code);

        String accessJSON = HttpclientUtil.doPost(url, tokenmap);

        Map<String, String> map1 = new HashMap<>();
        Map<String, String> map2 = JSON.parseObject(accessJSON, map1.getClass());

        String access_token = map2.get("access_token");
        String uid = map2.get("uid");

        //用access_token和用户id换取用户信息
        String addr4 = "https://api.weibo.com/2/users/show.json?access_token=" + access_token + "&uid=" + uid;//2.00VjTSyH77uCYDa279afeebayOdteC

        String userJson = HttpclientUtil.doGet(addr4);

        Map<String, Object> map3 = JSON.parseObject(userJson, map1.getClass());


        //保存第三方用户信息
        UmsMember umsMember = new UmsMember();
        umsMember.setSourceUid((String) map3.get("idstr"));
        umsMember.setSourceType("2");
        umsMember.setNickname((String) map3.get("screen_name"));
        umsMember.setUsername((String) map3.get("screen_name"));
        umsMember.setCity((String) map3.get("location"));
        umsMember.setAccessToken((String) map2.get("access_token"));
        umsMember.setCreateTime(new Date());

        UmsMember umsMemberForRequest = new UmsMember();
        UmsMember umsMemberExists = userService.isUserExists(umsMember);
        if (umsMemberExists == null) {
            UmsMember umsMember1 = userService.addUser(umsMember);
            umsMemberForRequest = umsMember1;
        } else {
            umsMemberForRequest = umsMemberExists;
        }


        //生成token,jwt,生成token
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("membetId", umsMemberForRequest.getId());
        userMap.put("nickname", umsMemberForRequest.getNickname());
        String remoteAddr = request.getRemoteAddr();
        String token = JwtUtil.encode("atguigugmall0722 ", userMap, remoteAddr);// 盐值


        //重定向到

        return "redirect:http://search.gmall.com:8083/index?newToken=" + token;
    }

    @RequestMapping("verify")
    @ResponseBody
    public Map<String, String> verify(String token, HttpServletRequest request, String currentIp) {
        Map<String, String> map = new HashMap<>();

        //1. cas校验用户凭证
        String userId = userService.getTokenCache(token);// 缓存校验
        if (StringUtils.isNotBlank(userId)) {
            // 校验成功返回用户信息
            map.put("success", "success");
            map.put("userId", userId);
        } else {
            map.put("success", "fail");
        }





        //String ip = request.getRemoteAddr();

        //2. jwt校验用户凭证
        String atguiguKey = "atguigugmall0722";
        Map<String, String> resultMap = JwtUtil.decode(atguiguKey, token, currentIp);//jwt校验

        if (resultMap != null) {
            // 校验成功返回用户信息
            map.put("success", "success");
            map.put("userId", (String) resultMap.get("userId"));
        } else {
            map.put("success", "fail");
        }
        return map;


    }

    @RequestMapping("index")
    public String index(String ReturnUrl, ModelMap modelMap) {

        modelMap.put("ReturnUrl", ReturnUrl);
        return "index";
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(HttpServletRequest request, UmsMember umsMember) {

        // 登录校验，调用userService，核对用户信息
        UmsMember user = userService.login(umsMember);
        //String userId = "1";

        if (user != null) {
            String userId = user.getId();
            // 校验成功返回用户登录凭证token
            String atguiguKey = "atguigugmall0722";
            String remoteAddr = request.getRemoteAddr();//?
            String ip = remoteAddr;

            Map<String, String> map = new HashMap<>();
            map.put("userId", userId);
            map.put("nickname", user.getNickname());

            String token = JwtUtil.encode(atguiguKey, map, ip);// 在有状态sso下，此处的jwt是一个生成token的工具

            // 将用户的token存储到redis缓存
            userService.putTokenCache(userId, token);
            return token;
        } else {
            return "fail";
        }
    }
}
