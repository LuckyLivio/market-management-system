<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>库存历史 - ${product.productName} - 超市管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>库存历史 - ${product.productName}</h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/inventory?action=list" class="btn btn-secondary">返回库存管理</a>
                <a href="${pageContext.request.contextPath}/inventory?action=in&productId=${product.productId}" class="btn btn-success">入库</a>
                <a href="${pageContext.request.contextPath}/inventory?action=out&productId=${product.productId}" class="btn btn-warning">出库</a>
            </div>
            
            <div class="product-info">
                <h3>商品信息</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>商品编号：</label>
                        <span>${product.productId}</span>
                    </div>
                    <div class="info-item">
                        <label>商品名称：</label>
                        <span>${product.productName}</span>
                    </div>
                    <div class="info-item">
                        <label>商品分类：</label>
                        <span>${product.category}</span>
                    </div>
                    <div class="info-item">
                        <label>当前库存：</label>
                        <span class="
                            <c:choose>
                                <c:when test="${product.stock <= 10}">text-danger</c:when>
                                <c:when test="${product.stock <= 20}">text-warning</c:when>
                                <c:otherwise>text-success</c:otherwise>
                            </c:choose>
                        ">
                            <strong>${product.stock}</strong>
                        </span>
                    </div>
                    <div class="info-item">
                        <label>单价：</label>
                        <span>￥<fmt:formatNumber value="${product.price}" pattern="#,##0.00"/></span>
                    </div>
                </div>
            </div>
            
            <div class="inventory-history">
                <h3>库存变动历史</h3>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>变动时间</th>
                            <th>变动类型</th>
                            <th>变动数量</th>
                            <th>变动原因</th>
                            <th>操作员</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="history" items="${inventoryHistory}">
                            <tr>
                                <td><fmt:formatDate value="${history.changeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${history.changeType == 'IN'}">
                                            <span class="badge badge-success">入库</span>
                                        </c:when>
                                        <c:when test="${history.changeType == 'OUT'}">
                                            <span class="badge badge-warning">出库</span>
                                        </c:when>
                                        <c:when test="${history.changeType == 'ADJUST'}">
                                            <span class="badge badge-info">调整</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${history.changeType == 'IN'}">
                                            <span class="text-success">+${history.quantity}</span>
                                        </c:when>
                                        <c:when test="${history.changeType == 'OUT'}">
                                            <span class="text-danger">-${history.quantity}</span>
                                        </c:when>
                                        <c:otherwise>
                                            ${history.quantity}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${history.reason}</td>
                                <td>${history.operator}</td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty inventoryHistory}">
                            <tr>
                                <td colspan="5" class="no-data">暂无库存变动记录</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>