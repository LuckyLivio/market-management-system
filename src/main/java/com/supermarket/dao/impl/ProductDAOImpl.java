package com.supermarket.dao.impl;

import com.supermarket.dao.ProductDAO;
import com.supermarket.model.Product;
import com.supermarket.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品数据访问实现类
 */
public class ProductDAOImpl implements ProductDAO {
    
    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO products (product_id, product_name, manufacturer, unit_price, stock_quantity, min_stock, stock_in_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setString(3, product.getManufacturer());
            pstmt.setBigDecimal(4, product.getUnitPrice());
            pstmt.setInt(5, product.getStockQuantity());
            pstmt.setInt(6, product.getMinStock());
            pstmt.setDate(7, product.getStockInDate());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean deleteProduct(String productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, productId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updateProduct(Product product) {
        String sql = "UPDATE products SET product_name=?, manufacturer=?, unit_price=?, stock_quantity=?, min_stock=?, stock_in_date=? WHERE product_id=?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getManufacturer());
            pstmt.setBigDecimal(3, product.getUnitPrice());
            pstmt.setInt(4, product.getStockQuantity());
            pstmt.setInt(5, product.getMinStock());
            pstmt.setDate(6, product.getStockInDate());
            pstmt.setString(7, product.getProductId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Product getProductById(String productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT * FROM products ORDER BY product_id";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    @Override
    public List<Product> getProductsByName(String productName) {
        String sql = "SELECT * FROM products WHERE product_name LIKE ? ORDER BY product_id";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + productName + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    @Override
    public List<Product> getLowStockProducts() {
        String sql = "SELECT * FROM products WHERE stock_quantity <= min_stock ORDER BY product_id";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return products;
    }
    
    @Override
    public boolean updateStock(String productId, int quantity) {
        String sql = "UPDATE products SET stock_quantity = stock_quantity + ? WHERE product_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productId);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 将ResultSet映射为Product对象
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setProductId(rs.getString("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setManufacturer(rs.getString("manufacturer"));
        product.setUnitPrice(rs.getBigDecimal("unit_price"));
        product.setStockQuantity(rs.getInt("stock_quantity"));
        product.setMinStock(rs.getInt("min_stock"));
        product.setStockInDate(rs.getDate("stock_in_date"));
        product.setCreatedAt(rs.getTimestamp("created_at"));
        product.setUpdatedAt(rs.getTimestamp("updated_at"));
        return product;
    }
}