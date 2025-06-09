package com.supermarket.dao;

import com.supermarket.model.User;

/**
 * 用户数据访问接口
 */
public interface UserDAO {
    
    /**
     * 根据用户名和密码查询用户
     */
    User getUserByUsernameAndPassword(String username, String password);
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
}