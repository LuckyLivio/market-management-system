<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>销售管理 - 超市管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>销售管理</h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/sale?action=new" class="btn btn-primary">新增销售</a>
                <a href="${pageContext.request.contextPath}/sale?action=report" class="btn btn-info">销售报表</a>
                <a href="${pageContext.request.contextPath}/product?action=list" class="btn btn-secondary">商品管理</a>
            </div>
            
            <!-- 搜索表单 -->
            <div class="search-form">
                <form method="post" action="${pageContext.request.contextPath}/sale">
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
                <div class="alert alert-success">销售记录添加成功！</div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <c:if test="${not empty totalAmount}">
                <div class="summary">
                    <h3>搜索结果统计</h3>
                    <p>总销售额：<span class="amount">￥<fmt:formatNumber value="${totalAmount}" pattern="#,##0.00"/></span></p>
                </div>
            </c:if>
            
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>销售编号</th>
                            <th>销售日期</th>
                            <th>客户姓名</th>
                            <th>销售金额</th>
                            <th>操作员</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="sale" items="${sales}">
                            <tr>
                                <td>${sale.saleId}</td>
                                <td><fmt:formatDate value="${sale.saleDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                <td>${sale.customerName}</td>
                                <td class="amount">￥<fmt:formatNumber value="${sale.totalAmount}" pattern="#,##0.00"/></td>
                                <td>${sale.operator}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/sale?action=detail&id=${sale.saleId}" class="btn btn-sm btn-info">查看详情</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sales}">
                            <tr>
                                <td colspan="6" class="no-data">暂无销售记录</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
            
            <!-- 分页 -->
            <c:if test="${totalPages > 1}">
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/sale?action=list&page=${currentPage-1}" class="btn btn-sm">上一页</a>
                    </c:if>
                    
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="btn btn-sm btn-primary">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/sale?action=list&page=${i}" class="btn btn-sm">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/sale?action=list&page=${currentPage+1}" class="btn btn-sm">下一页</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>
</body>
</html>