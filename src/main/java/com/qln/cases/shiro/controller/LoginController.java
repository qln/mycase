package com.qln.cases.shiro.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
public class LoginController {

    @ResponseBody
    @RequestMapping(value = "hello")
    public String hello(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        json.put("code", "200");
        json.put("msg", "hello");
        return json.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();

        Subject subject = SecurityUtils.getSubject();

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remeberMe = request.getParameter("remeberMe");

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            if ("true".equals(remeberMe)) {
                token.setRememberMe(true);
            }
            subject.login(token);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("code", "400");
            return json.toJSONString();
        }

        json.put("code", "200");
        json.put("msg", "ok");
        return json.toJSONString();
    }
}
