<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>自动生成资金占用记录</title>
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
        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
        }
        .form-group input:focus {
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
        .info-box {
            background: #e3f2fd;
            border: 1px solid #2196f3;
            border-radius: 5px;
            padding: 15px;
            margin-bottom: 20px;
        }
        .info-box h3 {
            margin-top: 0;
            color: #1976d2;
        }
        .info-box ul {
            margin: 10px 0;
            padding-left: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>自动生成资金占用记录</h1>
            <div class="user-info">
                <span>欢迎，${sessionScope.user.realName}</span>
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <div class="form-container">
                <div class="info-box">
                    <h3>功能说明</h3>
                    <p>此功能将根据当前库存商品自动计算并生成资金占用记录：</p>
                    <ul>
                        <li>按商品生产厂家分类统计资金占用</li>
                        <li>计算每个类别的占用金额（单价 × 库存量）</li>
                        <li>自动计算占用比例</li>
                        <li>为每个类别生成一条资金占用记录</li>
                    </ul>
                    <p><strong>注意：</strong>生成前请确保商品信息和库存数据准确。</p>
                </div>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>
                
                <form method="post" action="${pageContext.request.contextPath}/fund?action=generate">
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
                        <button type="submit" class="btn btn-success" 
                                onclick="return confirm('确定要生成资金占用记录吗？这将根据当前库存数据创建多条记录。')">生成记录</button>
                        <a href="${pageContext.request.contextPath}/fund?action=list" class="btn btn-secondary">取消</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>