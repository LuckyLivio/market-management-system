<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${action == 'in'}">入库操作</c:when>
            <c:when test="${action == 'out'}">出库操作</c:when>
            <c:when test="${action == 'adjust'}">库存调整</c:when>
        </c:choose>
        - 超市管理系统
    </title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>
                <c:choose>
                    <c:when test="${action == 'in'}">入库操作</c:when>
                    <c:when test="${action == 'out'}">出库操作</c:when>
                    <c:when test="${action == 'adjust'}">库存调整</c:when>
                </c:choose>
            </h1>
            <div class="user-info">
                欢迎，${sessionScope.username}
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/inventory?action=list" class="btn btn-secondary">返回库存管理</a>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <form method="post" action="${pageContext.request.contextPath}/inventory" class="form">
                <input type="hidden" name="action" value="${action}">
                
                <div class="form-section">
                    <div class="form-row">
                        <label for="productId">选择商品：</label>
                        <select id="productId" name="productId" required onchange="updateProductInfo()">
                            <option value="">请选择商品</option>
                            <c:forEach var="product" items="${products}">
                                <option value="${product.productId}" 
                                        data-stock="${product.stock}" 
                                        data-price="${product.price}">
                                    ${product.productName} (当前库存: ${product.stock})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-row">
                        <label for="currentStock">当前库存：</label>
                        <input type="text" id="currentStock" readonly class="readonly">
                    </div>
                    
                    <div class="form-row">
                        <label for="quantity">
                            <c:choose>
                                <c:when test="${action == 'in'}">入库数量：</c:when>
                                <c:when test="${action == 'out'}">出库数量：</c:when>
                                <c:when test="${action == 'adjust'}">调整后库存：</c:when>
                            </c:choose>
                        </label>
                        <input type="number" id="quantity" name="quantity" min="1" required>
                        <c:if test="${action == 'out'}">
                            <small class="form-help">注意：出库数量不能超过当前库存</small>
                        </c:if>
                    </div>
                    
                    <div class="form-row">
                        <label for="reason">操作原因：</label>
                        <textarea id="reason" name="reason" rows="3" required placeholder="请输入操作原因"></textarea>
                    </div>
                </div>
                
                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <c:choose>
                            <c:when test="${action == 'in'}">确认入库</c:when>
                            <c:when test="${action == 'out'}">确认出库</c:when>
                            <c:when test="${action == 'adjust'}">确认调整</c:when>
                        </c:choose>
                    </button>
                    <button type="reset" class="btn btn-secondary">重置</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        function updateProductInfo() {
            const select = document.getElementById('productId');
            const currentStockInput = document.getElementById('currentStock');
            const quantityInput = document.getElementById('quantity');
            
            if (select.value) {
                const selectedOption = select.options[select.selectedIndex];
                const stock = selectedOption.getAttribute('data-stock');
                currentStockInput.value = stock;
                
                // 如果是出库操作，设置最大值
                if ('${action}' === 'out') {
                    quantityInput.max = stock;
                }
            } else {
                currentStockInput.value = '';
                quantityInput.max = '';
            }
        }
        
        // 表单验证
        document.querySelector('form').addEventListener('submit', function(e) {
            const productSelect = document.getElementById('productId');
            const quantityInput = document.getElementById('quantity');
            
            if (!productSelect.value) {
                alert('请选择商品！');
                e.preventDefault();
                return;
            }
            
            const quantity = parseInt(quantityInput.value);
            if (quantity <= 0) {
                alert('数量必须大于0！');
                e.preventDefault();
                return;
            }
            
            // 出库验证
            if ('${action}' === 'out') {
                const selectedOption = productSelect.options[productSelect.selectedIndex];
                const stock = parseInt(selectedOption.getAttribute('data-stock'));
                
                if (quantity > stock) {
                    alert('出库数量不能超过当前库存！');
                    e.preventDefault();
                    return;
                }
            }
        });
    </script>
</body>
</html>