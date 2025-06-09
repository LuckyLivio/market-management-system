package com.supermarket.dao.impl;

import com.supermarket.dao.SaleDetailDAO;
import com.supermarket.model.SaleDetail;
import com.supermarket.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDetailDAOImpl implements SaleDetailDAO {
    
    @Override
    public boolean addSaleDetail(SaleDetail saleDetail) {
        String sql = "INSERT INTO sale_details (sale_id, product_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleDetail.getSaleId());
            pstmt.setInt(2, saleDetail.getProductId());
            pstmt.setInt(3, saleDetail.getQuantity());
            pstmt.setDouble(4, saleDetail.getUnitPrice());
            pstmt.setDouble(5, saleDetail.getSubtotal());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public List<SaleDetail> getSaleDetailsBySaleId(int saleId) {
        List<SaleDetail> details = new ArrayList<>();
        String sql = "SELECT sd.*, p.product_name FROM sale_details sd " +
                    "LEFT JOIN products p ON sd.product_id = p.product_id " +
                    "WHERE sd.sale_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SaleDetail detail = new SaleDetail();
                    detail.setDetailId(rs.getInt("detail_id"));
                    detail.setSaleId(rs.getInt("sale_id"));
                    detail.setProductId(rs.getInt("product_id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getDouble("unit_price"));
                    detail.setSubtotal(rs.getDouble("subtotal"));
                    detail.setProductName(rs.getString("product_name"));
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return details;
    }
    
    @Override
    public boolean deleteSaleDetailsBySaleId(int saleId) {
        String sql = "DELETE FROM sale_details WHERE sale_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, saleId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}