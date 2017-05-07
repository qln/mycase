package com.qln.cases.shiro.service;

import com.qln.cases.shiro.bean.User;

public interface UserService {
    
    User findByUsername(String username);
}
