<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>资金占用管理</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
    <style>
        .search-form {
            background: #f5f5f5;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .search-form .form-row {
            display: flex;
            gap: 15px;
            align-items: center;
            margin-bottom: 10px;
        }
        .search-form label {
            font-weight: bold;
            min-width: 80px;
        }
        .search-form select, .search-form input {
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        .ratio-high { color: #e74c3c; font-weight: bold; }
        .ratio-medium { color: #f39c12; }
        .ratio-low { color: #27ae60; }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>资金占用管理</h1>
            <div class="user-info">
                <span>欢迎，${sessionScope.user.realName}</span>
                <a href="${pageContext.request.contextPath}/login?action=logout">退出</a>
            </div>
        </div>
        
        <div class="content">
            <!-- 操作按钮 -->
            <div class="toolbar">
                <a href="${pageContext.request.contextPath}/fund?action=add" class="btn btn-primary">添加记录</a>
                <a href="${pageContext.request.contextPath}/fund?action=generate" class="btn btn-success">自动生成</a>
                <a href="${pageContext.request.contextPath}/fund?action=statistics" class="btn btn-info">统计报表</a>
                <a href="${pageContext.request.contextPath}/fund?action=category" class="btn btn-warning">分类统计</a>
            </div>
            
            <!-- 搜索表单 -->
            <form method="post" action="${pageContext.request.contextPath}/fund?action=search" class="search-form">
                <div class="form-row">
                    <label>搜索类型:</label>
                    <select name="searchType" id="searchType" onchange="toggleSearchFields()">
                        <option value="" ${empty searchType ? 'selected' : ''}>全部</option>
                        <option value="date" ${searchType == 'date' ? 'selected' : ''}>按日期范围</option>
                        <option value="category" ${searchType == 'category' ? 'selected' : ''}>按商品类型</option>
                    </select>
                </div>
                
                <div class="form-row" id="dateFields" style="display: ${searchType == 'date' ? 'flex' : 'none'}">
                    <label>开始日期:</label>
                    <input type="date" name="startDate" value="${startDate}">
                    <label>结束日期:</label>
                    <input type="date" name="endDate" value="${endDate}">
                </div>
                
                <div class="form-row" id="categoryField" style="display: ${searchType == 'category' ? 'flex' : 'none'}">
                    <label>商品类型:</label>
                    <select name="searchValue">
                        <option value="">请选择</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category}" ${searchValue == category ? 'selected' : ''}>${category}</option>
                        </c:forEach>
                    </select>
                </div>
                
                <div class="form-row">
                    <button type="submit" class="btn btn-primary">搜索</button>
                    <a href="${pageContext.request.contextPath}/fund?action=list" class="btn btn-secondary">重置</a>
                </div>
            </form>
            
            <!-- 消息提示 -->
            <c:if test="${not empty param.success}">
                <div class="alert alert-success">
                    <c:choose>
                        <c:when test="${param.success == 'add'}">资金占用记录添加成功！</c:when>
                        <c:when test="${param.success == 'update'}">资金占用记录更新成功！</c:when>
                        <c:when test="${param.success == 'delete'}">资金占用记录删除成功！</c:when>
                        <c:when test="${param.success == 'generate'}">资金占用记录生成成功！</c:when>
                    </c:choose>
                </div>
            </c:if>
            
            <c:if test="${not empty param.error}">
                <div class="alert alert-error">
                    <c:choose>
                        <c:when test="${param.error == 'notfound'}">记录不存在！</c:when>
                        <c:when test="${param.error == 'invalid'}">参数无效！</c:when>
                        <c:when test="${param.error == 'deletefail'}">删除失败！</c:when>
                        <c:otherwise>操作失败！</c:otherwise>
                    </c:choose>
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-error">${error}</div>
            </c:if>
            
            <!-- 资金占用列表 -->
            <div class="table-container">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>单号</th>
                            <th>商品类型</th>
                            <th>占用金额</th>
                            <th>总占用金额</th>
                            <th>占用比例</th>
                            <th>统计日期</th>
                            <th>会计</th>
                            <th>创建时间</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${empty fundOccupations}">
                                <tr>
                                    <td colspan="9" class="no-data">暂无资金占用记录</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="fund" items="${fundOccupations}">
                                    <tr>
                                        <td>${fund.recordId}</td>
                                        <td>${fund.productCategory}</td>
                                        <td>¥<fmt:formatNumber value="${fund.occupiedAmount}" pattern="#,##0.00"/></td>
                                        <td>¥<fmt:formatNumber value="${fund.totalOccupied}" pattern="#,##0.00"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${fund.occupationRatio >= 50}">
                                                    <span class="ratio-high"><fmt:formatNumber value="${fund.occupationRatio}" pattern="#0.00"/>%</span>
                                                </c:when>
                                                <c:when test="${fund.occupationRatio >= 20}">
                                                    <span class="ratio-medium"><fmt:formatNumber value="${fund.occupationRatio}" pattern="#0.00"/>%</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="ratio-low"><fmt:formatNumber value="${fund.occupationRatio}" pattern="#0.00"/>%</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><fmt:formatDate value="${fund.statisticsDate}" pattern="yyyy-MM-dd"/></td>
                                        <td>${fund.accountant}</td>
                                        <td><fmt:formatDate value="${fund.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td class="actions">
                                            <a href="${pageContext.request.contextPath}/fund?action=edit&id=${fund.recordId}" class="btn btn-sm btn-primary">编辑</a>
                                            <a href="${pageContext.request.contextPath}/fund?action=delete&id=${fund.recordId}" 
                                               class="btn btn-sm btn-danger" 
                                               onclick="return confirm('确定要删除这条记录吗？')">删除</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <script>
        function toggleSearchFields() {
            var searchType = document.getElementById('searchType').value;
            var dateFields = document.getElementById('dateFields');
            var categoryField = document.getElementById('categoryField');
            
            dateFields.style.display = searchType === 'date' ? 'flex' : 'none';
            categoryField.style.display = searchType === 'category' ? 'flex' : 'none';
        }
    </script>
</body>
</html>