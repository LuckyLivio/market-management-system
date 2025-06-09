package com.supermarket.servlet;

import com.supermarket.model.FundOccupation;
import com.supermarket.model.User;
import com.supermarket.service.FundOccupationService;
import com.supermarket.service.impl.FundOccupationServiceImpl;
import com.supermarket.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金占用控制器
 */
@WebServlet("/fund/*")
public class FundOccupationServlet extends HttpServlet {
    
    private FundOccupationService fundOccupationService;
    
    @Override
    public void init() throws ServletException {
        fundOccupationService = new FundOccupationServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listFundOccupations(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteFundOccupation(request, response);
                break;
            case "search":
                searchFundOccupations(request, response);
                break;
            case "statistics":
                showStatistics(request, response);
                break;
            case "generate":
                showGenerateForm(request, response);
                break;
            case "category":
                showCategoryStatistics(request, response);
                break;
            default:
                listFundOccupations(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/fund?action=list");
            return;
        }
        
        switch (action) {
            case "add":
                addFundOccupation(request, response);
                break;
            case "edit":
                updateFundOccupation(request, response);
                break;
            case "search":
                searchFundOccupations(request, response);
                break;
            case "generate":
                generateFundOccupation(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/fund?action=list");
                break;
        }
    }
    
    /**
     * 显示资金占用列表
     */
    private void listFundOccupations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<FundOccupation> fundOccupations = fundOccupationService.getAllFundOccupations();
        List<String> categories = fundOccupationService.getAvailableCategories();
        
        request.setAttribute("fundOccupations", fundOccupations);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/jsp/fund/fund_list.jsp").forward(request, response);
    }
    
    /**
     * 显示添加表单
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<String> categories = fundOccupationService.getAvailableCategories();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/jsp/fund/fund_add.jsp").forward(request, response);
    }
    
    /**
     * 添加资金占用记录
     */
    private void addFundOccupation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String productCategory = request.getParameter("productCategory");
            String occupiedAmountStr = request.getParameter("occupiedAmount");
            String totalOccupiedStr = request.getParameter("totalOccupied");
            String statisticsDateStr = request.getParameter("statisticsDate");
            String accountant = request.getParameter("accountant");
            
            // 参数验证
            if (productCategory == null || productCategory.trim().isEmpty() ||
                occupiedAmountStr == null || occupiedAmountStr.trim().isEmpty() ||
                totalOccupiedStr == null || totalOccupiedStr.trim().isEmpty() ||
                statisticsDateStr == null || statisticsDateStr.trim().isEmpty()) {
                
                request.setAttribute("error", "请填写所有必填字段");
                showAddForm(request, response);
                return;
            }
            
            BigDecimal occupiedAmount = new BigDecimal(occupiedAmountStr);
            BigDecimal totalOccupied = new BigDecimal(totalOccupiedStr);
            Date statisticsDate = DateUtil.parseDate(statisticsDateStr);
            
            if (occupiedAmount.compareTo(BigDecimal.ZERO) <= 0 || 
                totalOccupied.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("error", "金额必须大于0");
                showAddForm(request, response);
                return;
            }
            
            if (occupiedAmount.compareTo(totalOccupied) > 0) {
                request.setAttribute("error", "占用金额不能大于总占用金额");
                showAddForm(request, response);
                return;
            }
            
            FundOccupation fundOccupation = new FundOccupation();
            fundOccupation.setProductCategory(productCategory.trim());
            fundOccupation.setOccupiedAmount(occupiedAmount);
            fundOccupation.setTotalOccupied(totalOccupied);
            fundOccupation.setStatisticsDate(statisticsDate);
            fundOccupation.setAccountant(accountant != null ? accountant.trim() : null);
            
            if (fundOccupationService.addFundOccupation(fundOccupation)) {
                response.sendRedirect(request.getContextPath() + "/fund?action=list&success=add");
            } else {
                request.setAttribute("error", "添加资金占用记录失败");
                showAddForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "金额格式不正确");
            showAddForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "添加资金占用记录时发生错误: " + e.getMessage());
            showAddForm(request, response);
        }
    }
    
    /**
     * 显示编辑表单
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int recordId = Integer.parseInt(request.getParameter("id"));
            FundOccupation fundOccupation = fundOccupationService.getFundOccupationById(recordId);
            
            if (fundOccupation == null) {
                response.sendRedirect(request.getContextPath() + "/fund?action=list&error=notfound");
                return;
            }
            
            List<String> categories = fundOccupationService.getAvailableCategories();
            request.setAttribute("fundOccupation", fundOccupation);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/jsp/fund/fund_edit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/fund?action=list&error=invalid");
        }
    }
    
    /**
     * 更新资金占用记录
     */
    private void updateFundOccupation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int recordId = Integer.parseInt(request.getParameter("recordId"));
            String productCategory = request.getParameter("productCategory");
            String occupiedAmountStr = request.getParameter("occupiedAmount");
            String totalOccupiedStr = request.getParameter("totalOccupied");
            String statisticsDateStr = request.getParameter("statisticsDate");
            String accountant = request.getParameter("accountant");
            
            // 参数验证
            if (productCategory == null || productCategory.trim().isEmpty() ||
                occupiedAmountStr == null || occupiedAmountStr.trim().isEmpty() ||
                totalOccupiedStr == null || totalOccupiedStr.trim().isEmpty() ||
                statisticsDateStr == null || statisticsDateStr.trim().isEmpty()) {
                
                request.setAttribute("error", "请填写所有必填字段");
                showEditForm(request, response);
                return;
            }
            
            BigDecimal occupiedAmount = new BigDecimal(occupiedAmountStr);
            BigDecimal totalOccupied = new BigDecimal(totalOccupiedStr);
            Date statisticsDate = DateUtil.parseDate(statisticsDateStr);
            
            if (occupiedAmount.compareTo(BigDecimal.ZERO) <= 0 || 
                totalOccupied.compareTo(BigDecimal.ZERO) <= 0) {
                request.setAttribute("error", "金额必须大于0");
                showEditForm(request, response);
                return;
            }
            
            if (occupiedAmount.compareTo(totalOccupied) > 0) {
                request.setAttribute("error", "占用金额不能大于总占用金额");
                showEditForm(request, response);
                return;
            }
            
            FundOccupation fundOccupation = new FundOccupation();
            fundOccupation.setRecordId(recordId);
            fundOccupation.setProductCategory(productCategory.trim());
            fundOccupation.setOccupiedAmount(occupiedAmount);
            fundOccupation.setTotalOccupied(totalOccupied);
            fundOccupation.setStatisticsDate(statisticsDate);
            fundOccupation.setAccountant(accountant != null ? accountant.trim() : null);
            
            if (fundOccupationService.updateFundOccupation(fundOccupation)) {
                response.sendRedirect(request.getContextPath() + "/fund?action=list&success=update");
            } else {
                request.setAttribute("error", "更新资金占用记录失败");
                showEditForm(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "参数格式不正确");
            showEditForm(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "更新资金占用记录时发生错误: " + e.getMessage());
            showEditForm(request, response);
        }
    }
    
    /**
     * 删除资金占用记录
     */
    private void deleteFundOccupation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            int recordId = Integer.parseInt(request.getParameter("id"));
            
            if (fundOccupationService.deleteFundOccupation(recordId)) {
                response.sendRedirect(request.getContextPath() + "/fund?action=list&success=delete");
            } else {
                response.sendRedirect(request.getContextPath() + "/fund?action=list&error=deletefail");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/fund?action=list&error=invalid");
        }
    }
    
    /**
     * 搜索资金占用记录
     */
    private void searchFundOccupations(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String searchType = request.getParameter("searchType");
        String searchValue = request.getParameter("searchValue");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        List<FundOccupation> fundOccupations = null;
        
        try {
            if ("date".equals(searchType) && startDateStr != null && endDateStr != null) {
                Date startDate = DateUtil.parseDate(startDateStr);
                Date endDate = DateUtil.parseDate(endDateStr);
                fundOccupations = fundOccupationService.getFundOccupationsByDateRange(startDate, endDate);
            } else if ("category".equals(searchType) && searchValue != null && !searchValue.trim().isEmpty()) {
                fundOccupations = fundOccupationService.getFundOccupationsByCategory(searchValue.trim());
            } else {
                fundOccupations = fundOccupationService.getAllFundOccupations();
            }
            
            List<String> categories = fundOccupationService.getAvailableCategories();
            request.setAttribute("fundOccupations", fundOccupations);
            request.setAttribute("categories", categories);
            request.setAttribute("searchType", searchType);
            request.setAttribute("searchValue", searchValue);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            
            request.getRequestDispatcher("/jsp/fund/fund_list.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "搜索时发生错误: " + e.getMessage());
            listFundOccupations(request, response);
        }
    }
    
    /**
     * 显示统计信息
     */
    private void showStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        try {
            Date startDate, endDate;
            
            if (startDateStr != null && endDateStr != null) {
                startDate = DateUtil.parseDate(startDateStr);
                endDate = DateUtil.parseDate(endDateStr);
            } else {
                // 默认显示当月统计
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                startDate = new Date(cal.getTimeInMillis());
                
                cal.add(java.util.Calendar.MONTH, 1);
                cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
                endDate = new Date(cal.getTimeInMillis());
            }
            
            Map<String, Object> statistics = fundOccupationService.getFundOccupationStatistics(startDate, endDate);
            List<Map<String, Object>> categoryStats = fundOccupationService.getFundOccupationByCategory(startDate, endDate);
            
            request.setAttribute("statistics", statistics);
            request.setAttribute("categoryStats", categoryStats);
            request.setAttribute("startDate", DateUtil.formatDate(startDate));
            request.setAttribute("endDate", DateUtil.formatDate(endDate));
            
            request.getRequestDispatcher("/jsp/fund/fund_statistics.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "获取统计信息时发生错误: " + e.getMessage());
            listFundOccupations(request, response);
        }
    }
    
    /**
     * 显示生成记录表单
     */
    private void showGenerateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.getRequestDispatcher("/jsp/fund/fund_generate.jsp").forward(request, response);
    }
    
    /**
     * 自动生成资金占用记录
     */
    private void generateFundOccupation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String statisticsDateStr = request.getParameter("statisticsDate");
            String accountant = request.getParameter("accountant");
            
            if (statisticsDateStr == null || statisticsDateStr.trim().isEmpty()) {
                request.setAttribute("error", "请选择统计日期");
                showGenerateForm(request, response);
                return;
            }
            
            Date statisticsDate = DateUtil.parseDate(statisticsDateStr);
            
            // 获取当前用户作为默认会计
            if (accountant == null || accountant.trim().isEmpty()) {
                HttpSession session = request.getSession();
                User currentUser = (User) session.getAttribute("user");
                if (currentUser != null) {
                    accountant = currentUser.getRealName();
                }
            }
            
            if (fundOccupationService.generateFundOccupationRecord(statisticsDate, accountant)) {
                response.sendRedirect(request.getContextPath() + "/fund?action=list&success=generate");
            } else {
                request.setAttribute("error", "生成资金占用记录失败");
                showGenerateForm(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "生成资金占用记录时发生错误: " + e.getMessage());
            showGenerateForm(request, response);
        }
    }
    
    /**
     * 显示分类统计
     */
    private void showCategoryStatistics(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        
        try {
            Date startDate, endDate;
            
            if (startDateStr != null && endDateStr != null) {
                startDate = DateUtil.parseDate(startDateStr);
                endDate = DateUtil.parseDate(endDateStr);
            } else {
                // 默认显示当月统计
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
                startDate = new Date(cal.getTimeInMillis());
                
                cal.add(java.util.Calendar.MONTH, 1);
                cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
                endDate = new Date(cal.getTimeInMillis());
            }
            
            List<Map<String, Object>> categoryStats = fundOccupationService.getFundOccupationByCategory(startDate, endDate);
            
            request.setAttribute("categoryStats", categoryStats);
            request.setAttribute("startDate", DateUtil.formatDate(startDate));
            request.setAttribute("endDate", DateUtil.formatDate(endDate));
            
            request.getRequestDispatcher("/jsp/fund/fund_category.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "获取分类统计时发生错误: " + e.getMessage());
            listFundOccupations(request, response);
        }
    }
}