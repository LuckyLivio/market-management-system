<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>低库存提醒 - 超市管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>低库存提醒</h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/inventory?action=list" class="btn btn-secondary">返回库存管理</a>
            </div>
            
            <div class="search-form">
                <form method="get" action="${pageContext.request.contextPath}/inventory">
                    <input type="hidden" name="action" value="lowstock">
                    <div class="form-row">
                        <label>库存阈值：</label>
                        <input type="number" name="threshold" value="${threshold}" min="1" max="100">
                        <button type="submit" class="btn btn-primary">查询</button>
                    </div>
                </form>
            </div>
            
            <div class="alert alert-warning">
                <strong>提醒：</strong>以下商品库存低于 ${threshold} 件，请及时补货！
            </div>
            
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>商品编号</th>
                            <th>商品名称</th>
                            <th>分类</th>
                            <th>当前库存</th>
                            <th>单价</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="product" items="${lowStockProducts}">
                            <tr class="warning-row">
                                <td>${product.productId}</td>
                                <td>${product.productName}</td>
                                <td>${product.category}</td>
                                <td class="text-danger"><strong>${product.stock}</strong></td>
                                <td>￥<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/inventory?action=in&productId=${product.productId}" class="btn btn-sm btn-success">立即入库</a>
                                    <a href="${pageContext.request.contextPath}/inventory?action=history&productId=${product.productId}" class="btn btn-sm btn-info">查看历史</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty lowStockProducts}">
                            <tr>
                                <td colspan="6" class="no-data text-success">恭喜！当前没有低库存商品。</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <c:if test="${not empty lowStockProducts}">
                <div class="summary">
                    <p class="text-danger"><strong>共发现 ${lowStockProducts.size()} 个商品库存不足，请及时处理！</strong></p>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>