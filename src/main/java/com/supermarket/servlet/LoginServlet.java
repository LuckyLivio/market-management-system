package com.supermarket.servlet;

import com.supermarket.model.User;
import com.supermarket.service.UserService;
import com.supermarket.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录控制器
 */
public class LoginServlet extends HttpServlet {
    
    private UserService userService;
    
    @Override
    public void init() throws ServletException {
        userService = new UserServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 处理登出
        String action = request.getParameter("action");
        if ("logout".equals(action)) {
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        // 重定向到登录页面
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            User user = userService.login(username, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("main.jsp"); // 跳转到主页面
            } else {
                request.setAttribute("error", "用户名或密码错误");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "登录失败：" + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }
    
    /**
     * 根据用户角色获取重定向URL
     */
    private String getRedirectUrlByRole(String role) {
        switch (role) {
            case "销售员":
                return "/jsp/sale/sale_main.jsp";
            case "库管员":
                return "/jsp/inventory/inventory_main.jsp";
            case "财务员":
                return "/jsp/finance/finance_main.jsp";
            case "管理员":
                return "/jsp/admin/admin_main.jsp";
            default:
                return "/jsp/common/main.jsp";
        }
    }
}