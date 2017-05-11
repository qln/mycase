package com.qln.cases.elasticsearch.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "japi", produces = "text/json;charset=UTF-8")
public class JAPIController {

    @Autowired
    TransportClient transportClient;

    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String create(HttpServletRequest request, HttpServletResponse response) {
        JSONObject res = new JSONObject();
        IndexResponse resp = null;

        try {
            Map<String, Object> json = new HashMap<String, Object>();
            json.put("user", "kimchy");
            json.put("postDate", new Date());
            json.put("message", "trying out Elasticsearch");
            resp = transportClient.prepareIndex("twitter", "tweet", "1").setSource(json).get();
            System.out.println(resp.getIndex());
        } catch (Exception e) {
            e.printStackTrace();
        }
        res.put("code", "200");
        return res.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "bulk", method = RequestMethod.GET)
    public String bulk(HttpServletRequest request, HttpServletResponse response) {

        return "";
    }

    @ResponseBody
    @RequestMapping(value = "mapping", method = RequestMethod.GET)
    public String mapping(HttpServletRequest request, HttpServletResponse response) {
        XContentBuilder builder = null;
        PutMappingRequest mapping = null;
        try {
            builder = XContentFactory.jsonBuilder().startObject().startObject("tweet").startObject("properties").startObject("id").field("type", "integer").field("store", "yes").endObject()
                    .startObject("kw").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject().startObject("edate").field("type", "date")
                    .field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject().endObject().endObject().endObject();
            mapping = Requests.putMappingRequest("").type("").source(builder);
            transportClient.admin().indices().putMapping(mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public String get(HttpServletRequest request, HttpServletResponse response) {
        JSONObject res = new JSONObject();
        GetResponse resp = null;
        try {
            resp = transportClient.prepareGet("megacorp", "employee", "1").execute().actionGet();
            res = JSON.parseObject(resp.getSourceAsString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res.toJSONString();
    }
}
