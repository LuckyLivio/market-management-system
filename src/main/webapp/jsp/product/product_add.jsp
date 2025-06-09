<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.supermarket.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加商品 - 小型超市管理系统</title>
    <link rel="stylesheet" type="text/css" href="../../css/common.css">
    <link rel="stylesheet" type="text/css" href="../../css/form.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>添加商品</h1>
            <div class="user-info">
                欢迎，<%= user.getRealName() %> (<%= user.getRole() %>)
                <a href="../../login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <form action="../../product" method="post" class="form">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label for="productId">商品号：</label>
                    <input type="text" id="productId" name="productId" required>
                </div>
                
                <div class="form-group">
                    <label for="productName">商品名：</label>
                    <input type="text" id="productName" name="productName" required>
                </div>
                
                <div class="form-group">
                    <label for="manufacturer">生产厂家：</label>
                    <input type="text" id="manufacturer" name="manufacturer">
                </div>
                
                <div class="form-group">
                    <label for="unitPrice">单价：</label>
                    <input type="number" id="unitPrice" name="unitPrice" step="0.01" min="0" required>
                </div>
                
                <div class="form-group">
                    <label for="stockQuantity">库存量：</label>
                    <input type="number" id="stockQuantity" name="stockQuantity" min="0" required>
                </div>
                
                <div class="form-group">
                    <label for="minStock">最小库存量：</label>
                    <input type="number" id="minStock" name="minStock" min="0" required>
                </div>
                
                <div class="form-group">
                    <label for="stockInDate">入库日期：</label>
                    <input type="date" id="stockInDate" name="stockInDate" required>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">添加商品</button>
                    <a href="../../product?action=list" class="btn">返回列表</a>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        // 设置默认日期为今天
        document.getElementById('stockInDate').value = new Date().toISOString().split('T')[0];
    </script>
</body>
</html>