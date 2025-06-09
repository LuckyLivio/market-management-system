package com.supermarket.service.impl;

import com.supermarket.dao.UserDAO;
import com.supermarket.dao.impl.UserDAOImpl;
import com.supermarket.model.User;
import com.supermarket.service.UserService;

/**
 * 用户业务逻辑实现类
 */
public class UserServiceImpl implements UserService {
    
    private UserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            return null;
        }
        
        return userDAO.getUserByUsernameAndPassword(username.trim(), password);
    }
    
    @Override
    public User getUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        
        return userDAO.getUserByUsername(username.trim());
    }
    
    @Override
    public boolean hasPermission(User user, String requiredRole) {
        if (user == null || requiredRole == null) {
            return false;
        }
        
        // 管理员拥有所有权限
        if (User.ROLE_ADMIN.equals(user.getRole())) {
            return true;
        }
        
        // 检查用户角色是否匹配
        return requiredRole.equals(user.getRole());
    }
}