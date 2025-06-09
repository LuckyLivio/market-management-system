<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>销售详情 - 超市管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>销售详情</h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/sale?action=list" class="btn btn-secondary">返回销售列表</a>
                <button onclick="window.print()" class="btn btn-info">打印</button>
            </div>
            
            <div class="sale-info">
                <h3>销售信息</h3>
                <div class="info-grid">
                    <div class="info-item">
                        <label>销售编号：</label>
                        <span>${sale.saleId}</span>
                    </div>
                    <div class="info-item">
                        <label>销售日期：</label>
                        <span><fmt:formatDate value="${sale.saleDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                    </div>
                    <div class="info-item">
                        <label>客户姓名：</label>
                        <span>${sale.customerName}</span>
                    </div>
                    <div class="info-item">
                        <label>操作员：</label>
                        <span>${sale.operator}</span>
                    </div>
                    <div class="info-item">
                        <label>总金额：</label>
                        <span class="amount">￥<fmt:formatNumber value="${sale.totalAmount}" pattern="#,##0.00"/></span>
                    </div>
                </div>
            </div>
            
            <div class="sale-details">
                <h3>商品明细</h3>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>商品名称</th>
                            <th>单价</th>
                            <th>数量</th>
                            <th>小计</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${saleDetails}">
                            <tr>
                                <td>${detail.productName}</td>
                                <td>￥<fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0.00"/></td>
                                <td>${detail.quantity}</td>
                                <td>￥<fmt:formatNumber value="${detail.subtotal}" pattern="#,##0.00"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3"><strong>总计：</strong></td>
                            <td><strong>￥<fmt:formatNumber value="${sale.totalAmount}" pattern="#,##0.00"/></strong></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</body>
</html>