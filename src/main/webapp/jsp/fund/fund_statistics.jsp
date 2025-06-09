<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>资金占用统计</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .stats-container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 30px;
        }
        .stats-card {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .stats-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
            color: #333;
        }
        .stats-item {
            display: flex;
            justify-content: space-between;
            padding: 8px 0;
            border-bottom: 1px solid #eee;
        }
        .stats-item:last-child {
            border-bottom: none;
        }
        .stats-label {
            color: #666;
        }
        .stats-value {
            font-weight: bold;
            color: #2c5aa0;
        }
        .chart-container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .chart-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
            text-align: center;
            color: #333;
        }
        .chart-wrapper {
            position: relative;
            height: 400px;
        }
        .date-filter {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .filter-form {
            display: flex;
            gap: 15px;
            align-items: end;
        }
        .form-group {
            flex: 1;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>资金占用统计</h1>
            <div class="header-actions">
                <a href="${pageContext.request.contextPath}/fund?action=list" class="btn btn-secondary">返回列表</a>
            </div>
        </div>

        <!-- 日期筛选 -->
        <div class="date-filter">
            <form class="filter-form" method="get" action="${pageContext.request.contextPath}/fund">
                <input type="hidden" name="action" value="statistics">
                <div class="form-group">
                    <label for="startDate">开始日期：</label>
                    <input type="date" id="startDate" name="startDate" value="${param.startDate}">
                </div>
                <div class="form-group">
                    <label for="endDate">结束日期：</label>
                    <input type="date" id="endDate" name="endDate" value="${param.endDate}">
                </div>
                <button type="submit" class="btn btn-primary">查询</button>
            </form>
        </div>

        <!-- 统计卡片 -->
        <div class="stats-container">
            <div class="stats-card">
                <div class="stats-title">总体统计</div>
                <div class="stats-item">
                    <span class="stats-label">总资金占用：</span>
                    <span class="stats-value">￥<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/></span>
                </div>
                <div class="stats-item">
                    <span class="stats-label">记录总数：</span>
                    <span class="stats-value">${totalCount}</span>
                </div>
                <div class="stats-item">
                    <span class="stats-label">平均占用率：</span>
                    <span class="stats-value"><fmt:formatNumber value="${avgRatio}" pattern="#0.00"/>%</span>
                </div>
                <div class="stats-item">
                    <span class="stats-label">最高占用率：</span>
                    <span class="stats-value"><fmt:formatNumber value="${maxRatio}" pattern="#0.00"/>%</span>
                </div>
            </div>

            <div class="stats-card">
                <div class="stats-title">分类统计</div>
                <c:forEach var="category" items="${categoryStats}">
                    <div class="stats-item">
                        <span class="stats-label">${category.category}：</span>
                        <span class="stats-value">￥<fmt:formatNumber value="${category.amount}" pattern="#,##0.00"/></span>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- 图表区域 -->
        <div class="chart-container">
            <div class="chart-title">资金占用趋势图</div>
            <div class="chart-wrapper">
                <canvas id="trendChart"></canvas>
            </div>
        </div>

        <div class="chart-container">
            <div class="chart-title">分类占比图</div>
            <div class="chart-wrapper">
                <canvas id="categoryChart"></canvas>
            </div>
        </div>
    </div>

    <script>
        // 趋势图数据
        const trendData = {
            labels: [<c:forEach var="item" items="${trendData}" varStatus="status">'${item.date}'<c:if test="${!status.last}">,</c:if></c:forEach>],
            datasets: [{
                label: '资金占用金额',
                data: [<c:forEach var="item" items="${trendData}" varStatus="status">${item.amount}<c:if test="${!status.last}">,</c:if></c:forEach>],
                borderColor: 'rgb(44, 90, 160)',
                backgroundColor: 'rgba(44, 90, 160, 0.1)',
                tension: 0.1,
                fill: true
            }]
        };

        // 分类数据
        const categoryData = {
            labels: [<c:forEach var="category" items="${categoryStats}" varStatus="status">'${category.category}'<c:if test="${!status.last}">,</c:if></c:forEach>],
            datasets: [{
                data: [<c:forEach var="category" items="${categoryStats}" varStatus="status">${category.amount}<c:if test="${!status.last}">,</c:if></c:forEach>],
                backgroundColor: [
                    '#FF6384',
                    '#36A2EB',
                    '#FFCE56',
                    '#4BC0C0',
                    '#9966FF',
                    '#FF9F40',
                    '#FF6384',
                    '#C9CBCF'
                ]
            }]
        };

        // 创建趋势图
        const trendCtx = document.getElementById('trendChart').getContext('2d');
        new Chart(trendCtx, {
            type: 'line',
            data: trendData,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '￥' + value.toLocaleString();
                            }
                        }
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return context.dataset.label + ': ￥' + context.parsed.y.toLocaleString();
                            }
                        }
                    }
                }
            }
        });

        // 创建分类饼图
        const categoryCtx = document.getElementById('categoryChart').getContext('2d');
        new Chart(categoryCtx, {
            type: 'pie',
            data: categoryData,
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = ((context.parsed / total) * 100).toFixed(1);
                                return context.label + ': ￥' + context.parsed.toLocaleString() + ' (' + percentage + '%)';
                            }
                        }
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });

        // 设置默认日期
        window.onload = function() {
            const today = new Date();
            const lastMonth = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            
            if (!document.getElementById('startDate').value) {
                document.getElementById('startDate').value = lastMonth.toISOString().split('T')[0];
            }
            if (!document.getElementById('endDate').value) {
                document.getElementById('endDate').value = today.toISOString().split('T')[0];
            }
        };
    </script>
</body>
</html>