<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加资金占用记录</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        .form-group input:focus, .form-group select:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0,123,255,0.3);
        }
        .required {
            color: red;
        }
        .form-actions {
            text-align: center;
            margin-top: 30px;
        }
        .ratio-display {
            font-weight: bold;
            color: #007bff;
            margin-top: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>添加资金占用记录</h1>
            <div class="user-info">
                <span>欢迎，${sessionScope.user.realName}</span>
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="form-container">
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>
                
                <form method="post" action="${pageContext.request.contextPath}/fund?action=add">
                    <div class="form-group">
                        <label for="productCategory">商品类型 <span class="required">*</span></label>
                        <select id="productCategory" name="productCategory" required>
                            <option value="">请选择商品类型</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category}">${category}</option>
                            </c:forEach>
                            <option value="其他">其他</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="occupiedAmount">占用金额 <span class="required">*</span></label>
                        <input type="number" id="occupiedAmount" name="occupiedAmount" 
                               step="0.01" min="0" placeholder="请输入占用金额" 
                               onchange="calculateRatio()" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="totalOccupied">总占用金额 <span class="required">*</span></label>
                        <input type="number" id="totalOccupied" name="totalOccupied" 
                               step="0.01" min="0" placeholder="请输入总占用金额" 
                               onchange="calculateRatio()" required>
                    </div>
                    
                    <div class="form-group">
                        <label>占用比例</label>
                        <div id="ratioDisplay" class="ratio-display">请输入金额后自动计算</div>
                    </div>
                    
                    <div class="form-group">
                        <label for="statisticsDate">统计日期 <span class="required">*</span></label>
                        <input type="date" id="statisticsDate" name="statisticsDate" 
                               value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="accountant">会计</label>
                        <input type="text" id="accountant" name="accountant" 
                               placeholder="请输入会计姓名" value="${sessionScope.user.realName}">
                    </div>
                    
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">保存</button>
                        <a href="${pageContext.request.contextPath}/fund?action=list" class="btn btn-secondary">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <script>
        function calculateRatio() {
            var occupiedAmount = parseFloat(document.getElementById('occupiedAmount').value) || 0;
            var totalOccupied = parseFloat(document.getElementById('totalOccupied').value) || 0;
            var ratioDisplay = document.getElementById('ratioDisplay');
            
            if (totalOccupied > 0) {
                var ratio = (occupiedAmount / totalOccupied * 100).toFixed(2);
                ratioDisplay.textContent = ratio + '%';
                
                if (ratio >= 50) {
                    ratioDisplay.style.color = '#e74c3c';
                } else if (ratio >= 20) {
                    ratioDisplay.style.color = '#f39c12';
                } else {
                    ratioDisplay.style.color = '#27ae60';
                }
            } else {
                ratioDisplay.textContent = '请输入金额后自动计算';
                ratioDisplay.style.color = '#007bff';
            }
        }
        
        // 验证占用金额不能大于总占用金额
        document.querySelector('form').addEventListener('submit', function(e) {
            var occupiedAmount = parseFloat(document.getElementById('occupiedAmount').value) || 0;
            var totalOccupied = parseFloat(document.getElementById('totalOccupied').value) || 0;
            
            if (occupiedAmount > totalOccupied) {
                alert('占用金额不能大于总占用金额！');
                e.preventDefault();
                return false;
            }
        });
    </script>
</body>
</html>