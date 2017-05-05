package com.qln.cases.shiro.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qln.cases.shiro.bean.User;

public interface UserDao extends JpaRepository<User, Long>{
    
    User findByUsername(String username);

}
