<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .navbar {
        background: #2c3e50;
        padding: 0;
        margin-bottom: 20px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    .navbar-brand {
        color: white;
        font-size: 18px;
        font-weight: bold;
        padding: 15px 20px;
        text-decoration: none;
        display: inline-block;
    }
    .navbar-nav {
        list-style: none;
        margin: 0;
        padding: 0;
        display: inline-flex;
    }
    .nav-item {
        position: relative;
    }
    .nav-link {
        color: #ecf0f1;
        padding: 15px 20px;
        text-decoration: none;
        display: block;
        transition: background-color 0.3s;
    }
    .nav-link:hover {
        background: #34495e;
        color: #3498db;
    }
    .dropdown-menu {
        position: absolute;
        top: 100%;
        left: 0;
        background: #34495e;
        min-width: 200px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.2);
        display: none;
        z-index: 1000;
    }
    .nav-item:hover .dropdown-menu {
        display: block;
    }
    .dropdown-item {
        color: #ecf0f1;
        padding: 10px 20px;
        text-decoration: none;
        display: block;
        font-size: 14px;
    }
    .dropdown-item:hover {
        background: #3498db;
        color: white;
    }
    .navbar-right {
        float: right;
        margin-right: 20px;
        line-height: 50px;
    }
    .user-info {
        color: #bdc3c7;
        margin-right: 15px;
    }
    .logout-link {
        color: #e74c3c;
        text-decoration: none;
        padding: 5px 10px;
        border: 1px solid #e74c3c;
        border-radius: 3px;
        transition: all 0.3s;
    }
    .logout-link:hover {
        background: #e74c3c;
        color: white;
    }
</style>

<nav class="navbar">
    <a href="${pageContext.request.contextPath}/main.jsp" class="navbar-brand">超市管理系统</a>
    
    <ul class="navbar-nav">
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/main.jsp" class="nav-link">首页</a>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">商品管理</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/product?action=list" class="dropdown-item">商品列表</a>
                <a href="${pageContext.request.contextPath}/product?action=add" class="dropdown-item">添加商品</a>
            </div>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">销售管理</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/sale?action=list" class="dropdown-item">销售记录</a>
                <a href="${pageContext.request.contextPath}/sale?action=add" class="dropdown-item">新增销售</a>
                <a href="${pageContext.request.contextPath}/sale?action=report" class="dropdown-item">销售报表</a>
            </div>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">库存管理</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/inventory?action=list" class="dropdown-item">库存变动</a>
                <a href="${pageContext.request.contextPath}/inventory?action=inStock" class="dropdown-item">商品入库</a>
                <a href="${pageContext.request.contextPath}/inventory?action=outStock" class="dropdown-item">商品出库</a>
                <a href="${pageContext.request.contextPath}/inventory?action=adjust" class="dropdown-item">库存调整</a>
                <a href="${pageContext.request.contextPath}/inventory?action=lowStock" class="dropdown-item">低库存预警</a>
            </div>
        </li>
        <li class="nav-item">
            <a href="#" class="nav-link">财务管理</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/fund?action=list" class="dropdown-item">资金占用</a>
                <a href="${pageContext.request.contextPath}/fund?action=add" class="dropdown-item">添加记录</a>
                <a href="${pageContext.request.contextPath}/fund?action=generate" class="dropdown-item">生成记录</a>
                <a href="${pageContext.request.contextPath}/fund?action=statistics" class="dropdown-item">统计报表</a>
            </div>
        </li>
    </ul>
    
    <div class="navbar-right">
        <span class="user-info">欢迎，${sessionScope.user.realName} (${sessionScope.user.role})</span>
        <a href="${pageContext.request.contextPath}/logout" class="logout-link">退出</a>
    </div>
    <div style="clear: both;"></div>
</nav>

<script>
// 导航菜单交互增强
document.addEventListener('DOMContentLoaded', function() {
    // 高亮当前页面对应的导航项
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.nav-link, .dropdown-item');
    
    navLinks.forEach(link => {
        if (link.getAttribute('href') && currentPath.includes(link.getAttribute('href'))) {
            link.style.background = '#3498db';
            link.style.color = 'white';
        }
    });
});
</script>