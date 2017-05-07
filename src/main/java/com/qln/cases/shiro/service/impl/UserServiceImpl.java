package com.qln.cases.shiro.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qln.cases.shiro.bean.User;
import com.qln.cases.shiro.dao.UserDao;
import com.qln.cases.shiro.service.UserService;

@Service
public class UserServiceImpl implements UserService{
    
    @Autowired
    UserDao userDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

}
