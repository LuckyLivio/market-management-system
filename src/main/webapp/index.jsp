<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>小型超市管理系统 - 登录</title>
    <link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
    <div class="login-container">
        <div class="login-box">
            <h2>小型超市管理系统</h2>
            <form action="login" method="post">
                <div class="input-group">
                    <label for="username">用户名：</label>
                    <input type="text" id="username" name="username" required>
                </div>
                <div class="input-group">
                    <label for="password">密码：</label>
                    <input type="password" id="password" name="password" required>
                </div>
                <div class="input-group">
                    <button type="submit">登录</button>
                </div>
            </form>
            
            <% if (request.getAttribute("error") != null) { %>
                <div class="error-message">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <div class="demo-accounts">
                <h3>测试账号：</h3>
                <p>管理员：admin / admin123</p>
                <p>销售员：cashier1 / cash123</p>
                <p>库管员：warehouse1 / ware123</p>
                <p>财务员：finance1 / fin123</p>
            </div>
        </div>
    </div>
</body>
</html>