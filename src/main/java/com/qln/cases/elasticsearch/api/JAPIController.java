package com.qln.cases.elasticsearch.api;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.qln.cases.elasticsearch.api.util.ESUtil;

@Controller
@RequestMapping("client")
public class JAPIController {

    @Autowired
    TransportClient transportClient;

    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    public String create(HttpServletRequest request, HttpServletResponse response) {
        JSONObject res = new JSONObject();
        IndexResponse resp = null;
        TransportClient client = null;
        try {
            // client = ESUtil.getTransportClient();
            client = transportClient;

            Map<String, Object> json = new HashMap<String, Object>();
            json.put("user", "kimchy");
            json.put("postDate", new Date());
            json.put("message", "trying out Elasticsearch");
            resp = client.prepareIndex("twitter", "tweet", "1").setSource(json).get();
            System.out.println(resp.getIndex());
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.put("code", "200");
        return res.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    public String get(HttpServletRequest request, HttpServletResponse response) {
        JSONObject res = new JSONObject();
        TransportClient client = null;
        GetResponse resp = null;
        try {
            // client = ESUtil.getTransportClient();
            client = transportClient;

            resp = client.prepareGet("megacorp", "employee", "1").execute().actionGet();
            System.out.println(resp.getSourceAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.put("code", "200");
        return res.toJSONString();
    }
}
