package com.qln.cases.elasticsearch.restapi;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "restapi")
public class RestController {
    private static final Logger logger = LoggerFactory.getLogger(RestController.class);

    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    public String search(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RestClient restClient = RestClient.builder(new HttpHost("192.168.11.219", 9200, "http")).build();
        Response res = restClient.performRequest("GET", "/", Collections.singletonMap("pretty", "true"));
        logger.info(EntityUtils.toString(res.getEntity()));

        JSONObject result = new JSONObject();
        result.put("code", "200");
        return result.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    public String create(HttpServletRequest request, HttpServletResponse response) {
        RestClient restClient = RestClient.builder(new HttpHost("192.168.11.219", 9200, "http")).build();
        JSONObject data = new JSONObject();
        data.put("user", "kimchy");
        data.put("post_date", "2009-11-15T14:12:12");
        data.put("message", "trying out Elasticsearch");
        HttpEntity entity = new NStringEntity(data.toJSONString(), ContentType.APPLICATION_JSON);
        try {
            Response indexResponse = restClient.performRequest("PUT", "/twitter/tweet/1", Collections.<String, String> emptyMap(), entity);
            logger.info("create response:{}", EntityUtils.toString(indexResponse.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject result = new JSONObject();
        result.put("code", "200");
        return result.toJSONString();
    }
}
