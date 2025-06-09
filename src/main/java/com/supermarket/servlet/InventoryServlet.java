package com.supermarket.servlet;

import com.supermarket.model.InventoryChange;
import com.supermarket.model.Product;
import com.supermarket.service.InventoryService;
import com.supermarket.service.ProductService;
import com.supermarket.service.impl.InventoryServiceImpl;
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
import java.util.Date;
import java.util.List;

@WebServlet("/inventory")
public class InventoryServlet extends HttpServlet {
    private InventoryService inventoryService = new InventoryServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("list".equals(action)) {
            listInventoryChanges(request, response);
        } else if ("adjust".equals(action)) {
            showAdjustForm(request, response);
        } else if ("in".equals(action)) {
            showInStockForm(request, response);
        } else if ("out".equals(action)) {
            showOutStockForm(request, response);
        } else if ("lowstock".equals(action)) {
            showLowStockProducts(request, response);
        } else if ("history".equals(action)) {
            showProductHistory(request, response);
        } else {
            listInventoryChanges(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("adjust".equals(action)) {
            processAdjustInventory(request, response);
        } else if ("in".equals(action)) {
            processInStock(request, response);
        } else if ("out".equals(action)) {
            processOutStock(request, response);
        } else if ("search".equals(action)) {
            searchInventoryChanges(request, response);
        }
    }
    
    private void listInventoryChanges(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<InventoryChange> changes = inventoryService.getAllInventoryChanges();
        request.setAttribute("inventoryChanges", changes);
        request.getRequestDispatcher("/jsp/inventory/inventory_list.jsp").forward(request, response);
    }
    
    private void showAdjustForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.setAttribute("action", "adjust");
        request.getRequestDispatcher("/jsp/inventory/inventory_form.jsp").forward(request, response);
    }
    
    private void showInStockForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.setAttribute("action", "in");
        request.getRequestDispatcher("/jsp/inventory/inventory_form.jsp").forward(request, response);
    }
    
    private void showOutStockForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.setAttribute("action", "out");
        request.getRequestDispatcher("/jsp/inventory/inventory_form.jsp").forward(request, response);
    }
    
    private void showLowStockProducts(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int threshold = 10; // 默认低库存阈值
        String thresholdStr = request.getParameter("threshold");
        if (!StringUtil.isEmpty(thresholdStr)) {
            threshold = Integer.parseInt(thresholdStr);
        }
        
        List<Product> lowStockProducts = inventoryService.getLowStockProducts(threshold);
        request.setAttribute("lowStockProducts", lowStockProducts);
        request.setAttribute("threshold", threshold);
        request.getRequestDispatcher("/jsp/inventory/low_stock.jsp").forward(request, response);
    }
    
    private void showProductHistory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            List<InventoryChange> history = inventoryService.getInventoryHistory(productId);
            Product product = productService.getProductById(productId);
            
            request.setAttribute("inventoryHistory", history);
            request.setAttribute("product", product);
            request.getRequestDispatcher("/jsp/inventory/product_history.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/inventory?action=list");
        }
    }
    
    private void processAdjustInventory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String operator = (String) session.getAttribute("username");
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String reason = request.getParameter("reason");
            
            if (inventoryService.adjustInventory(productId, quantity, reason, operator)) {
                response.sendRedirect(request.getContextPath() + "/inventory?action=list&success=1");
            } else {
                request.setAttribute("error", "库存调整失败");
                showAdjustForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "库存调整出错：" + e.getMessage());
            showAdjustForm(request, response);
        }
    }
    
    private void processInStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String operator = (String) session.getAttribute("username");
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String reason = request.getParameter("reason");
            
            if (inventoryService.inStock(productId, quantity, reason, operator)) {
                response.sendRedirect(request.getContextPath() + "/inventory?action=list&success=1");
            } else {
                request.setAttribute("error", "入库操作失败");
                showInStockForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "入库操作出错：" + e.getMessage());
            showInStockForm(request, response);
        }
    }
    
    private void processOutStock(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            String operator = (String) session.getAttribute("username");
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            String reason = request.getParameter("reason");
            
            if (inventoryService.outStock(productId, quantity, reason, operator)) {
                response.sendRedirect(request.getContextPath() + "/inventory?action=list&success=1");
            } else {
                request.setAttribute("error", "出库操作失败，可能是库存不足");
                showOutStockForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "出库操作出错：" + e.getMessage());
            showOutStockForm(request, response);
        }
    }
    
    private void searchInventoryChanges(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            
            Date startDate = DateUtil.parseDate(startDateStr);
            Date endDate = DateUtil.parseDate(endDateStr);
            
            List<InventoryChange> changes = inventoryService.getInventoryChangesByDateRange(startDate, endDate);
            
            request.setAttribute("inventoryChanges", changes);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            
            request.getRequestDispatcher("/jsp/inventory/inventory_list.jsp").forward(request, response);
        } catch (ParseException e) {
            e.printStackTrace();
            request.setAttribute("error", "日期格式错误");
            listInventoryChanges(request, response);
        }
    }
}