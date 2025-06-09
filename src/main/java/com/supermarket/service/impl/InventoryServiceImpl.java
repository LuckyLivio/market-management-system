package com.supermarket.service.impl;

import com.supermarket.dao.InventoryChangeDAO;
import com.supermarket.dao.ProductDAO;
import com.supermarket.dao.impl.InventoryChangeDAOImpl;
import com.supermarket.dao.impl.ProductDAOImpl;
import com.supermarket.model.InventoryChange;
import com.supermarket.model.Product;
import com.supermarket.service.InventoryService;
import com.supermarket.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class InventoryServiceImpl implements InventoryService {
    private InventoryChangeDAO inventoryChangeDAO = new InventoryChangeDAOImpl();
    private ProductDAO productDAO = new ProductDAOImpl();
    
    @Override
    public boolean adjustInventory(int productId, int quantity, String reason, String operator) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            // 获取当前产品信息
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                conn.rollback();
                return false;
            }
            
            // 更新库存
            product.setStock(quantity);
            if (!productDAO.updateProduct(product)) {
                conn.rollback();
                return false;
            }
            
            // 记录库存变动
            InventoryChange change = new InventoryChange(productId, "ADJUST", quantity - product.getStock(), reason, operator);
            if (!inventoryChangeDAO.addInventoryChange(change)) {
                conn.rollback();
                return false;
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
    public boolean inStock(int productId, int quantity, String reason, String operator) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            // 获取当前产品信息
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                conn.rollback();
                return false;
            }
            
            // 增加库存
            int newStock = product.getStock() + quantity;
            product.setStock(newStock);
            if (!productDAO.updateProduct(product)) {
                conn.rollback();
                return false;
            }
            
            // 记录库存变动
            InventoryChange change = new InventoryChange(productId, "IN", quantity, reason, operator);
            if (!inventoryChangeDAO.addInventoryChange(change)) {
                conn.rollback();
                return false;
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
    public boolean outStock(int productId, int quantity, String reason, String operator) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            // 获取当前产品信息
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                conn.rollback();
                return false;
            }
            
            // 检查库存是否足够
            if (product.getStock() < quantity) {
                conn.rollback();
                return false; // 库存不足
            }
            
            // 减少库存
            int newStock = product.getStock() - quantity;
            product.setStock(newStock);
            if (!productDAO.updateProduct(product)) {
                conn.rollback();
                return false;
            }
            
            // 记录库存变动
            InventoryChange change = new InventoryChange(productId, "OUT", quantity, reason, operator);
            if (!inventoryChangeDAO.addInventoryChange(change)) {
                conn.rollback();
                return false;
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
    public List<InventoryChange> getInventoryHistory(int productId) {
        return inventoryChangeDAO.getInventoryChangesByProductId(productId);
    }
    
    @Override
    public List<InventoryChange> getInventoryChangesByDateRange(Date startDate, Date endDate) {
        return inventoryChangeDAO.getInventoryChangesByDateRange(startDate, endDate);
    }
    
    @Override
    public List<Product> getLowStockProducts(int threshold) {
        return productDAO.getLowStockProducts(threshold);
    }
    
    @Override
    public List<InventoryChange> getAllInventoryChanges() {
        return inventoryChangeDAO.getAllInventoryChanges();
    }
}