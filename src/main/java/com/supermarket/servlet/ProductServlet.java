package com.supermarket.servlet;

import com.supermarket.model.Product;
import com.supermarket.service.ProductService;
import com.supermarket.service.impl.ProductServiceImpl;
import com.supermarket.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * 商品管理控制器
 */
public class ProductServlet extends HttpServlet {
    
    private ProductService productService;
    
    @Override
    public void init() throws ServletException {
        productService = new ProductServiceImpl();
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
                listProducts(request, response);
                break;
            case "search":
                searchProducts(request, response);
                break;
            case "edit":
                editProduct(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "lowStock":
                lowStockProducts(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "add":
                addProduct(request, response);
                break;
            case "update":
                updateProduct(request, response);
                break;
            default:
                doGet(request, response);
                break;
        }
    }
    
    /**
     * 查询所有商品
     */
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/jsp/product/product_list.jsp").forward(request, response);
    }
    
    /**
     * 搜索商品
     */
    private void searchProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String keyword = request.getParameter("keyword");
        List<Product> products = productService.searchProductsByName(keyword);
        request.setAttribute("products", products);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/jsp/product/product_list.jsp").forward(request, response);
    }
    
    /**
     * 编辑商品
     */
    private void editProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("productId");
        Product product = productService.getProductById(productId);
        request.setAttribute("product", product);
        request.getRequestDispatcher("/jsp/product/product_edit.jsp").forward(request, response);
    }
    
    /**
     * 删除商品
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String productId = request.getParameter("productId");
        boolean success = productService.deleteProduct(productId);
        
        if (success) {
            request.setAttribute("message", "商品删除成功！");
        } else {
            request.setAttribute("error", "商品删除失败！");
        }
        
        listProducts(request, response);
    }
    
    /**
     * 查询库存不足商品
     */
    private void lowStockProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Product> products = productService.getLowStockProducts();
        request.setAttribute("products", products);
        request.setAttribute("isLowStock", true);
        request.getRequestDispatcher("/jsp/product/product_list.jsp").forward(request, response);
    }
    
    /**
     * 添加商品
     */
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Product product = new Product();
            product.setProductId(request.getParameter("productId"));
            product.setProductName(request.getParameter("productName"));
            product.setManufacturer(request.getParameter("manufacturer"));
            product.setUnitPrice(new BigDecimal(request.getParameter("unitPrice")));
            product.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
            product.setMinStock(Integer.parseInt(request.getParameter("minStock")));
            product.setStockInDate(Date.valueOf(request.getParameter("stockInDate")));
            
            boolean success = productService.addProduct(product);
            
            if (success) {
                request.setAttribute("message", "商品添加成功！");
            } else {
                request.setAttribute("error", "商品添加失败！商品号可能已存在。");
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "数据格式错误：" + e.getMessage());
        }
        
        listProducts(request, response);
    }
    
    /**
     * 更新商品
     */
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            Product product = new Product();
            product.setProductId(request.getParameter("productId"));
            product.setProductName(request.getParameter("productName"));
            product.setManufacturer(request.getParameter("manufacturer"));
            product.setUnitPrice(new BigDecimal(request.getParameter("unitPrice")));
            product.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
            product.setMinStock(Integer.parseInt(request.getParameter("minStock")));
            product.setStockInDate(Date.valueOf(request.getParameter("stockInDate")));
            
            boolean success = productService.updateProduct(product);
            
            if (success) {
                request.setAttribute("message", "商品更新成功！");
            } else {
                request.setAttribute("error", "商品更新失败！");
            }
            
        } catch (Exception e) {
            request.setAttribute("error", "数据格式错误：" + e.getMessage());
        }
        
        listProducts(request, response);
    }
}