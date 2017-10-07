package com.qln.cases.elasticsearch.jestapi;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.mapping.PutMapping;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("jest")
public class JestController {

    @Autowired
    JestClient client;

    @ResponseBody
    @RequestMapping(value = "get", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ApiOperation(notes = "查询", value = "查询")
    public String get(HttpServletRequest request, HttpServletResponse response) {
        JSONObject query = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("match_all", new JSONObject());
        query.put("query", data);

        Search search = new Search.Builder(query.toJSONString()).addIndex("megacorp").addType("employee").build();

        String res = "";
        try {
            SearchResult result = client.execute(search);
            res = result.getJsonObject().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    @ResponseBody
    @RequestMapping(value="mapping",method=RequestMethod.POST,produces="text/json;charset=UTF-8")
    public String mapping(HttpServletRequest request,HttpServletResponse response){
        PutMapping putMapping = new PutMapping.Builder("twitter", "tweet", "").build();
        try {
            client.execute(putMapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    public String create(HttpServletRequest request, HttpServletResponse response) {
        JSONObject source = new JSONObject();
        source.put("", "");
        source.put("", "");
        source.put("", "");
        Index index = new Index.Builder(source).index("").type("").build();
        DocumentResult result = null;
        try {
            result = client.execute(index);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject res = new JSONObject();
        res.put("code", "200");
        return res.toJSONString();
    }
}