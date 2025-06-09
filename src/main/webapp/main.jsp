<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>小型超市管理系统 - 主页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <style>
        .main-container {
            display: flex;
            min-height: 100vh;
        }
        .sidebar {
            width: 250px;
            background: #2c3e50;
            color: white;
            padding: 0;
            box-shadow: 2px 0 5px rgba(0,0,0,0.1);
        }
        .sidebar-header {
            padding: 20px;
            background: #34495e;
            border-bottom: 1px solid #3d566e;
        }
        .sidebar-header h2 {
            margin: 0;
            font-size: 18px;
            color: #ecf0f1;
        }
        .user-info {
            margin-top: 10px;
            font-size: 14px;
            color: #bdc3c7;
        }
        .nav-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .nav-item {
            border-bottom: 1px solid #3d566e;
        }
        .nav-link {
            display: block;
            padding: 15px 20px;
            color: #ecf0f1;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        .nav-link:hover {
            background: #34495e;
            color: #3498db;
        }
        .nav-link.active {
            background: #3498db;
            color: white;
        }
        .nav-submenu {
            list-style: none;
            padding: 0;
            margin: 0;
            background: #34495e;
            display: none;
        }
        .nav-item:hover .nav-submenu {
            display: block;
        }
        .nav-submenu .nav-link {
            padding-left: 40px;
            font-size: 14px;
        }
        .main-content {
            flex: 1;
            background: #ecf0f1;
        }
        .top-bar {
            background: white;
            padding: 15px 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .breadcrumb {
            color: #7f8c8d;
            font-size: 14px;
        }
        .user-actions {
            display: flex;
            gap: 15px;
            align-items: center;
        }
        .content-area {
            padding: 30px;
        }
        .dashboard-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .dashboard-card {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s;
        }
        .dashboard-card:hover {
            transform: translateY(-2px);
        }
        .card-icon {
            font-size: 48px;
            margin-bottom: 15px;
        }
        .card-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 10px;
            color: #2c3e50;
        }
        .card-value {
            font-size: 24px;
            font-weight: bold;
            color: #3498db;
        }
        .quick-actions {
            background: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .quick-actions h3 {
            margin-top: 0;
            margin-bottom: 20px;
            color: #2c3e50;
        }
        .action-buttons {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }
        .action-btn {
            display: block;
            padding: 15px;
            background: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            text-align: center;
            transition: background-color 0.3s;
        }
        .action-btn:hover {
            background: #2980b9;
        }
        .logout-btn {
            background: #e74c3c;
            color: white;
            border: none;
            padding: 8px 15px;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }
        .logout-btn:hover {
            background: #c0392b;
        }
    </style>
</head>
<body>
    <div class="main-container">
        <!-- 侧边栏 -->
        <div class="sidebar">
            <div class="sidebar-header">
                <h2>超市管理系统</h2>
                <div class="user-info">
                    欢迎，${sessionScope.user.realName}
                    <br>角色：${sessionScope.user.role}
                </div>
            </div>
            <ul class="nav-menu">
                <li class="nav-item">
                    <a href="${pageContext.request.contextPath}/main.jsp" class="nav-link active">🏠 首页</a>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">📦 商品管理</a>
                    <ul class="nav-submenu">
                        <li><a href="${pageContext.request.contextPath}/product?action=list" class="nav-link">商品列表</a></li>
                        <li><a href="${pageContext.request.contextPath}/product?action=add" class="nav-link">添加商品</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">💰 销售管理</a>
                    <ul class="nav-submenu">
                        <li><a href="${pageContext.request.contextPath}/sale?action=list" class="nav-link">销售记录</a></li>
                        <li><a href="${pageContext.request.contextPath}/sale?action=add" class="nav-link">新增销售</a></li>
                        <li><a href="${pageContext.request.contextPath}/sale?action=report" class="nav-link">销售报表</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">📋 库存管理</a>
                    <ul class="nav-submenu">
                        <li><a href="${pageContext.request.contextPath}/inventory?action=list" class="nav-link">库存变动</a></li>
                        <li><a href="${pageContext.request.contextPath}/inventory?action=inStock" class="nav-link">商品入库</a></li>
                        <li><a href="${pageContext.request.contextPath}/inventory?action=outStock" class="nav-link">商品出库</a></li>
                        <li><a href="${pageContext.request.contextPath}/inventory?action=adjust" class="nav-link">库存调整</a></li>
                        <li><a href="${pageContext.request.contextPath}/inventory?action=lowStock" class="nav-link">低库存预警</a></li>
                    </ul>
                </li>
                <li class="nav-item">
                    <a href="#" class="nav-link">💼 财务管理</a>
                    <ul class="nav-submenu">
                        <li><a href="${pageContext.request.contextPath}/fund?action=list" class="nav-link">资金占用</a></li>
                        <li><a href="${pageContext.request.contextPath}/fund?action=add" class="nav-link">添加记录</a></li>
                        <li><a href="${pageContext.request.contextPath}/fund?action=generate" class="nav-link">生成记录</a></li>
                        <li><a href="${pageContext.request.contextPath}/fund?action=statistics" class="nav-link">统计报表</a></li>
                    </ul>
                </li>
            </ul>
        </div>

        <!-- 主内容区 -->
        <div class="main-content">
            <div class="top-bar">
                <div class="breadcrumb">
                    首页 / 仪表板
                </div>
                <div class="user-actions">
                    <span>当前时间：<span id="currentTime"></span></span>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">退出登录</a>
                </div>
            </div>

            <div class="content-area">
                <h1>系统概览</h1>
                
                <!-- 统计卡片 -->
                <div class="dashboard-cards">
                    <div class="dashboard-card">
                        <div class="card-icon">📦</div>
                        <div class="card-title">商品总数</div>
                        <div class="card-value" id="productCount">-</div>
                    </div>
                    <div class="dashboard-card">
                        <div class="card-icon">💰</div>
                        <div class="card-title">今日销售额</div>
                        <div class="card-value" id="todaySales">-</div>
                    </div>
                    <div class="dashboard-card">
                        <div class="card-icon">📋</div>
                        <div class="card-title">库存总值</div>
                        <div class="card-value" id="inventoryValue">-</div>
                    </div>
                    <div class="dashboard-card">
                        <div class="card-icon">⚠️</div>
                        <div class="card-title">低库存商品</div>
                        <div class="card-value" id="lowStockCount">-</div>
                    </div>
                </div>

                <!-- 快捷操作 -->
                <div class="quick-actions">
                    <h3>快捷操作</h3>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/product?action=add" class="action-btn">添加商品</a>
                        <a href="${pageContext.request.contextPath}/sale?action=add" class="action-btn">新增销售</a>
                        <a href="${pageContext.request.contextPath}/inventory?action=inStock" class="action-btn">商品入库</a>
                        <a href="${pageContext.request.contextPath}/fund?action=generate" class="action-btn">生成财务记录</a>
                        <a href="${pageContext.request.contextPath}/inventory?action=lowStock" class="action-btn">查看低库存</a>
                        <a href="${pageContext.request.contextPath}/sale?action=report" class="action-btn">销售报表</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // 更新当前时间
        function updateTime() {
            const now = new Date();
            const timeString = now.toLocaleString('zh-CN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
            document.getElementById('currentTime').textContent = timeString;
        }

        // 加载统计数据
        function loadDashboardData() {
            // 这里可以通过AJAX加载实际的统计数据
            // 暂时使用模拟数据
            document.getElementById('productCount').textContent = '156';
            document.getElementById('todaySales').textContent = '￥12,345';
            document.getElementById('inventoryValue').textContent = '￥89,012';
            document.getElementById('lowStockCount').textContent = '8';
        }

        // 页面加载完成后执行
        window.onload = function() {
            updateTime();
            setInterval(updateTime, 1000); // 每秒更新时间
            loadDashboardData();
        };

        // 导航菜单交互
        document.querySelectorAll('.nav-item').forEach(item => {
            item.addEventListener('mouseenter', function() {
                const submenu = this.querySelector('.nav-submenu');
                if (submenu) {
                    submenu.style.display = 'block';
                }
            });
            
            item.addEventListener('mouseleave', function() {
                const submenu = this.querySelector('.nav-submenu');
                if (submenu) {
                    submenu.style.display = 'none';
                }
            });
        });
    </script>
</body>
</html>