package com.supermarket.service;

import com.supermarket.model.User;

/**
 * 用户业务逻辑接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    User login(String username, String password);
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    
    /**
     * 验证用户权限
     */
    boolean hasPermission(User user, String requiredRole);
}