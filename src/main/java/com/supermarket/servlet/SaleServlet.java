package com.supermarket.servlet;

import com.supermarket.model.Sale;
import com.supermarket.model.SaleDetail;
import com.supermarket.model.Product;
import com.supermarket.service.SaleService;
import com.supermarket.service.ProductService;
import com.supermarket.service.impl.SaleServiceImpl;
import com.supermarket.service.impl.ProductServiceImpl;
import com.supermarket.util.DateUtil;
import com.supermarket.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@WebServlet("/sale")
public class SaleServlet extends HttpServlet {
    private SaleService saleService = new SaleServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("list".equals(action)) {
            listSales(request, response);
        } else if ("detail".equals(action)) {
            viewSaleDetail(request, response);
        } else if ("new".equals(action)) {
            showNewSaleForm(request, response);
        } else if ("report".equals(action)) {
            showSalesReport(request, response);
        } else {
            listSales(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            processSale(request, response);
        } else if ("search".equals(action)) {
            searchSales(request, response);
        }
    }
    
    private void listSales(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int page = 1;
            int pageSize = 10;
            
            String pageStr = request.getParameter("page");
            if (!StringUtil.isEmpty(pageStr)) {
                page = Integer.parseInt(pageStr);
            }
            
            List<Sale> sales = saleService.getSalesByPage(page, pageSize);
            int totalCount = saleService.getTotalSalesCount();
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);
            
            request.setAttribute("sales", sales);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCount", totalCount);
            
            request.getRequestDispatcher("/jsp/sale/sale_list.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/sale?action=list");
        }
    }
    
    private void viewSaleDetail(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int saleId = Integer.parseInt(request.getParameter("id"));
            Sale sale = saleService.getSaleById(saleId);
            List<SaleDetail> details = saleService.getSaleDetails(saleId);
            
            request.setAttribute("sale", sale);
            request.setAttribute("saleDetails", details);
            
            request.getRequestDispatcher("/jsp/sale/sale_detail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/sale?action=list");
        }
    }
    
    private void showNewSaleForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/jsp/sale/sale_add.jsp").forward(request, response);
    }
    
    private void processSale(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String operator = (String) session.getAttribute("username");
            
            // 创建销售记录
            Sale sale = new Sale();
            sale.setSaleDate(new Date());
            sale.setCustomerName(request.getParameter("customerName"));
            sale.setOperator(operator);
            
            // 创建销售详情列表
            List<SaleDetail> saleDetails = new ArrayList<>();
            String[] productIds = request.getParameterValues("productId");
            String[] quantities = request.getParameterValues("quantity");
            String[] unitPrices = request.getParameterValues("unitPrice");
            
            double totalAmount = 0.0;
            
            if (productIds != null) {
                for (int i = 0; i < productIds.length; i++) {
                    if (!StringUtil.isEmpty(productIds[i]) && !StringUtil.isEmpty(quantities[i])) {
                        SaleDetail detail = new SaleDetail();
                        detail.setProductId(Integer.parseInt(productIds[i]));
                        detail.setQuantity(Integer.parseInt(quantities[i]));
                        detail.setUnitPrice(Double.parseDouble(unitPrices[i]));
                        detail.setSubtotal(detail.getQuantity() * detail.getUnitPrice());
                        
                        saleDetails.add(detail);
                        totalAmount += detail.getSubtotal();
                    }
                }
            }
            
            sale.setTotalAmount(totalAmount);
            
            // 处理销售
            if (saleService.processSale(sale, saleDetails)) {
                response.sendRedirect(request.getContextPath() + "/sale?action=list&success=1");
            } else {
                request.setAttribute("error", "销售处理失败，可能是库存不足");
                showNewSaleForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "销售处理出错：" + e.getMessage());
            showNewSaleForm(request, response);
        }
    }
    
    private void searchSales(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            Date startDate = DateUtil.parseDate(startDateStr);
            Date endDate = DateUtil.parseDate(endDateStr);
            
            List<Sale> sales = saleService.getSalesByDateRange(startDate, endDate);
            double totalAmount = saleService.getTotalSalesAmount(startDate, endDate);
            
            request.setAttribute("sales", sales);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            
            request.getRequestDispatcher("/jsp/sale/sale_list.jsp").forward(request, response);
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "日期格式错误");
            listSales(request, response);
        }
    }
    
    private void showSalesReport(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 获取今日销售数据
        Date today = new Date();
        Date startOfDay = DateUtil.getStartOfDay(today);
        Date endOfDay = DateUtil.getEndOfDay(today);
        
        double todaySales = saleService.getTotalSalesAmount(startOfDay, endOfDay);
        List<Sale> todaySalesList = saleService.getSalesByDateRange(startOfDay, endOfDay);
        
        request.setAttribute("todaySales", todaySales);
        request.setAttribute("todaySalesList", todaySalesList);
        request.setAttribute("reportDate", DateUtil.formatDate(today));
        
        request.getRequestDispatcher("/jsp/sale/sales_report.jsp").forward(request, response);
    }
}