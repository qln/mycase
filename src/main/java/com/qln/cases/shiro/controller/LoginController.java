package com.qln.cases.shiro.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "ok");
        return json.toJSONString();
    }
}
