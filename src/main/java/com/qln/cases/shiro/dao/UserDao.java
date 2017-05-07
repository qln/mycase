package com.qln.cases.shiro.dao;

import com.qln.cases.shiro.bean.User;

public interface UserDao{
    
    User findByUsername(String username);

}
