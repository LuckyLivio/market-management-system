<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>库存管理 - 超市管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>库存管理</h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/inventory?action=in" class="btn btn-success">入库</a>
                <a href="${pageContext.request.contextPath}/inventory?action=out" class="btn btn-warning">出库</a>
                <a href="${pageContext.request.contextPath}/inventory?action=adjust" class="btn btn-info">库存调整</a>
                <a href="${pageContext.request.contextPath}/inventory?action=lowstock" class="btn btn-danger">低库存提醒</a>
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn btn-secondary">商品管理</a>
                <a href="${pageContext.request.contextPath}/sale?action=list" class="btn btn-secondary">销售管理</a>
            </div>
            
            <!-- 搜索表单 -->
            <div class="search-form">
                <form method="post" action="${pageContext.request.contextPath}/inventory">
                    <input type="hidden" name="action" value="search">
                    <div class="form-row">
                        <label>开始日期：</label>
                        <input type="date" name="startDate" value="${startDate}">
                        <label>结束日期：</label>
                        <input type="date" name="endDate" value="${endDate}">
                        <button type="submit" class="btn btn-primary">搜索</button>
                    </div>
                </form>
            </div>
            
            <c:if test="${not empty param.success}">
                <div class="alert alert-success">操作成功！</div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>变动编号</th>
                            <th>商品名称</th>
                            <th>变动类型</th>
                            <th>变动数量</th>
                            <th>变动原因</th>
                            <th>变动时间</th>
                            <th>操作员</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="change" items="${inventoryChanges}">
                            <tr>
                                <td>${change.changeId}</td>
                                <td>${change.productName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${change.changeType == 'IN'}">
                                            <span class="badge badge-success">入库</span>
                                        </c:when>
                                        <c:when test="${change.changeType == 'OUT'}">
                                            <span class="badge badge-warning">出库</span>
                                        </c:when>
                                        <c:when test="${change.changeType == 'ADJUST'}">
                                            <span class="badge badge-info">调整</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${change.changeType == 'IN'}">
                                            <span class="text-success">+${change.quantity}</span>
                                        </c:when>
                                        <c:when test="${change.changeType == 'OUT'}">
                                            <span class="text-danger">-${change.quantity}</span>
                                        </c:when>
                                        <c:otherwise>
                                            ${change.quantity}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${change.reason}</td>
                                <td><fmt:formatDate value="${change.changeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>${change.operator}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/inventory?action=history&productId=${change.productId}" class="btn btn-sm btn-info">查看历史</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty inventoryChanges}">
                            <tr>
                                <td colspan="8" class="no-data">暂无库存变动记录</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>