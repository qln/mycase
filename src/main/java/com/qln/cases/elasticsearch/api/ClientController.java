package com.qln.cases.elasticsearch.api;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("client")
public class ClientController {

    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    public String create(HttpServletRequest request, HttpServletResponse response) {
        JSONObject res = new JSONObject();
        TransportClient client = null;
        try {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("user", "kimchy");
            json.put("postDate", new Date());
            json.put("message", "trying out Elasticsearch");
            Settings settings = Settings.builder().put("cluster.name", "qln-es").put("client.transport.sniff", true).build();
            client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.220.130"), 9300));
            IndexResponse resp = client.prepareIndex("twitter", "tweet", "1").setSource(json).get();
            System.out.println(resp.getIndex());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (client != null) {
                client.close();
            }
        }
        res.put("code", "200");
        return res.toJSONString();
    }
}
