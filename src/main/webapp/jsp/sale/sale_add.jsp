<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>新增销售 - 超市管理系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <script src="${pageContext.request.contextPath}/js/sale.js"></script>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>新增销售</h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/sale?action=list" class="btn btn-secondary">返回销售列表</a>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <form method="post" action="${pageContext.request.contextPath}/sale" id="saleForm">
                <input type="hidden" name="action" value="add">
                
                <div class="form-section">
                    <h3>销售信息</h3>
                    <div class="form-row">
                        <label for="customerName">客户姓名：</label>
                        <input type="text" id="customerName" name="customerName" required>
                    </div>
                </div>
                
                <div class="form-section">
                    <h3>商品明细</h3>
                    <div class="toolbar">
                        <button type="button" onclick="addProductRow()" class="btn btn-primary">添加商品</button>
                    </div>
                    
                    <table class="data-table" id="productTable">
                        <thead>
                            <tr>
                                <th>商品</th>
                                <th>单价</th>
                                <th>数量</th>
                                <th>小计</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody id="productTableBody">
                            <!-- 动态添加的商品行 -->
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="3"><strong>总计：</strong></td>
                                <td><strong id="totalAmount">￥0.00</strong></td>
                                <td></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">提交销售</button>
                    <button type="reset" class="btn btn-secondary">重置</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- 商品数据 -->
    <script>
        var products = [
            <c:forEach var="product" items="${products}" varStatus="status">
                {
                    id: ${product.productId},
                    name: '${product.productName}',
                    price: ${product.price},
                    stock: ${product.stock}
                }<c:if test="${!status.last}">,</c:if>
            </c:forEach>
        ];
    </script>
</body>
</html>