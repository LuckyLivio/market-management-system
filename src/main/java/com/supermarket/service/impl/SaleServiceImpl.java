package com.supermarket.service.impl;

import com.supermarket.dao.SaleDAO;
import com.supermarket.dao.SaleDetailDAO;
import com.supermarket.dao.ProductDAO;
import com.supermarket.dao.impl.SaleDAOImpl;
import com.supermarket.dao.impl.SaleDetailDAOImpl;
import com.supermarket.dao.impl.ProductDAOImpl;
import com.supermarket.model.Sale;
import com.supermarket.model.SaleDetail;
import com.supermarket.model.Product;
import com.supermarket.service.SaleService;
import com.supermarket.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SaleServiceImpl implements SaleService {
    private SaleDAO saleDAO = new SaleDAOImpl();
    private SaleDetailDAO saleDetailDAO = new SaleDetailDAOImpl();
    private ProductDAO productDAO = new ProductDAOImpl();
    
    @Override
    public boolean processSale(Sale sale, List<SaleDetail> saleDetails) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            // 1. 添加销售记录
            if (!saleDAO.addSale(sale)) {
                conn.rollback();
                return false;
            }
            
            // 2. 添加销售详情并更新库存
            for (SaleDetail detail : saleDetails) {
                detail.setSaleId(sale.getSaleId());
                
                // 添加销售详情
                if (!saleDetailDAO.addSaleDetail(detail)) {
                    conn.rollback();
                    return false;
                }
                
                // 更新产品库存
                Product product = productDAO.getProductById(detail.getProductId());
                if (product != null) {
                    int newStock = product.getStock() - detail.getQuantity();
                    if (newStock < 0) {
                        conn.rollback();
                        return false; // 库存不足
                    }
                    product.setStock(newStock);
                    if (!productDAO.updateProduct(product)) {
                        conn.rollback();
                        return false;
                    }
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    @Override
    public List<Sale> getAllSales() {
        return saleDAO.getAllSales();
    }
    
    @Override
    public List<Sale> getSalesByDateRange(Date startDate, Date endDate) {
        return saleDAO.getSalesByDateRange(startDate, endDate);
    }
    
    @Override
    public Sale getSaleById(int saleId) {
        return saleDAO.getSaleById(saleId);
    }
    
    @Override
    public List<SaleDetail> getSaleDetails(int saleId) {
        return saleDetailDAO.getSaleDetailsBySaleId(saleId);
    }
    
    @Override
    public double getTotalSalesAmount(Date startDate, Date endDate) {
        return saleDAO.getTotalSalesAmount(startDate, endDate);
    }
    
    @Override
    public List<Sale> getSalesByPage(int page, int pageSize) {
        return saleDAO.getSalesByPage(page, pageSize);
    }
    
    @Override
    public int getTotalSalesCount() {
        return saleDAO.getTotalSalesCount();
    }
}