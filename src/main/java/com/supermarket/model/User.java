package com.supermarket.model;

import java.sql.Timestamp;

/**
 * 用户实体类
 */
public class User {
    private Integer userId;         // 用户ID
    private String username;        // 用户名
    private String password;        // 密码
    private String role;           // 角色
    private String realName;       // 真实姓名
    private Timestamp createdAt;   // 创建时间
    
    // 角色常量
    public static final String ROLE_ADMIN = "管理员";
    public static final String ROLE_CASHIER = "销售员";
    public static final String ROLE_WAREHOUSE = "库管员";
    public static final String ROLE_FINANCE = "财务员";
    
    // 无参构造函数
    public User() {}
    
    // 构造函数
    public User(String username, String password, String role, String realName) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.realName = realName;
    }
    
    // Getter和Setter方法
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}