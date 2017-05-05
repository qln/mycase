package com.qln.cases.shiro.credentials;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class CustomCredentialsMatcher extends HashedCredentialsMatcher{

    private static final Logger logger = LoggerFactory.getLogger(CustomCredentialsMatcher.class);
    
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        logger.info("token:{}",JSON.toJSONString(token));
        logger.info("info:{}",JSON.toJSONString(info));
        return true;
    }
      
}
