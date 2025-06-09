<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.supermarket.model.Product" %>
<%@ page import="com.supermarket.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
    
    List<Product> products = (List<Product>) request.getAttribute("products");
    String keyword = (String) request.getAttribute("keyword");
    Boolean isLowStock = (Boolean) request.getAttribute("isLowStock");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>商品管理 - 小型超市管理系统</title>
    <link rel="stylesheet" type="text/css" href="../../css/common.css">
    <link rel="stylesheet" type="text/css" href="../../css/product.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>商品管理</h1>
            <div class="user-info">
                欢迎，<%= user.getRealName() %> (<%= user.getRole() %>)
                <a href="../../login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="toolbar">
            <div class="search-box">
                <form action="../../product" method="get">
                    <input type="hidden" name="action" value="search">
                    <input type="text" name="keyword" placeholder="搜索商品名称" value="<%= keyword != null ? keyword : "" %>">
                    <button type="submit">搜索</button>
                </form>
            </div>
            
            <div class="action-buttons">
                <a href="../../product?action=list" class="btn">所有商品</a>
                <a href="../../product?action=lowStock" class="btn btn-warning">库存不足</a>
                <a href="product_add.jsp" class="btn btn-primary">添加商品</a>
            </div>
        </div>
        
        <% if (request.getAttribute("message") != null) { %>
            <div class="message success">
                <%= request.getAttribute("message") %>
            </div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="message error">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>
        
        <div class="content">
            <% if (isLowStock != null && isLowStock) { %>
                <h2 style="color: red;">库存不足商品列表</h2>
            <% } else if (keyword != null && !keyword.trim().isEmpty()) { %>
                <h2>搜索结果："<%= keyword %>"</h2>
            <% } else { %>
                <h2>商品列表</h2>
            <% } %>
            
            <table class="data-table">
                <thead>
                    <tr>
                        <th>商品号</th>
                        <th>商品名</th>
                        <th>生产厂家</th>
                        <th>单价</th>
                        <th>库存量</th>
                        <th>最小库存</th>
                        <th>入库日期</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <% if (products != null && !products.isEmpty()) { %>
                        <% for (Product product : products) { %>
                            <tr <% if (product.getStockQuantity() <= product.getMinStock()) { %>class="low-stock"<% } %>>
                                <td><%= product.getProductId() %></td>
                                <td><%= product.getProductName() %></td>
                                <td><%= product.getManufacturer() %></td>
                                <td>¥<%= product.getUnitPrice() %></td>
                                <td><%= product.getStockQuantity() %></td>
                                <td><%= product.getMinStock() %></td>
                                <td><%= product.getStockInDate() %></td>
                                <td>
                                    <a href="../../product?action=edit&productId=<%= product.getProductId() %>" class="btn btn-small">编辑</a>
                                    <a href="../../product?action=delete&productId=<%= product.getProductId() %>" 
                                       class="btn btn-small btn-danger" 
                                       onclick="return confirm('确定要删除这个商品吗？')">删除</a>
                                </td>
                            </tr>
                        <% } %>
                    <% } else { %>
                        <tr>
                            <td colspan="8" class="no-data">暂无商品数据</td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>