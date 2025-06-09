package com.supermarket.dao.impl;

import com.supermarket.dao.InventoryChangeDAO;
import com.supermarket.model.InventoryChange;
import com.supermarket.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InventoryChangeDAOImpl implements InventoryChangeDAO {
    
    @Override
    public boolean addInventoryChange(InventoryChange change) {
        String sql = "INSERT INTO inventory_changes (product_id, change_type, quantity, reason, change_date, operator) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, change.getProductId());
            pstmt.setString(2, change.getChangeType());
            pstmt.setInt(3, change.getQuantity());
            pstmt.setString(4, change.getReason());
            pstmt.setTimestamp(5, new Timestamp(change.getChangeDate().getTime()));
            pstmt.setString(6, change.getOperator());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public List<InventoryChange> getInventoryChangesByProductId(int productId) {
        List<InventoryChange> changes = new ArrayList<>();
        String sql = "SELECT ic.*, p.product_name FROM inventory_changes ic " +
                    "LEFT JOIN products p ON ic.product_id = p.product_id " +
                    "WHERE ic.product_id = ? ORDER BY ic.change_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InventoryChange change = new InventoryChange();
                    change.setChangeId(rs.getInt("change_id"));
                    change.setProductId(rs.getInt("product_id"));
                    change.setChangeType(rs.getString("change_type"));
                    change.setQuantity(rs.getInt("quantity"));
                    change.setReason(rs.getString("reason"));
                    change.setChangeDate(rs.getTimestamp("change_date"));
                    change.setOperator(rs.getString("operator"));
                    change.setProductName(rs.getString("product_name"));
                    changes.add(change);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return changes;
    }
    
    @Override
    public List<InventoryChange> getInventoryChangesByDateRange(Date startDate, Date endDate) {
        List<InventoryChange> changes = new ArrayList<>();
        String sql = "SELECT ic.*, p.product_name FROM inventory_changes ic " +
                    "LEFT JOIN products p ON ic.product_id = p.product_id " +
                    "WHERE ic.change_date BETWEEN ? AND ? ORDER BY ic.change_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            pstmt.setTimestamp(2, new Timestamp(endDate.getTime()));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    InventoryChange change = new InventoryChange();
                    change.setChangeId(rs.getInt("change_id"));
                    change.setProductId(rs.getInt("product_id"));
                    change.setChangeType(rs.getString("change_type"));
                    change.setQuantity(rs.getInt("quantity"));
                    change.setReason(rs.getString("reason"));
                    change.setChangeDate(rs.getTimestamp("change_date"));
                    change.setOperator(rs.getString("operator"));
                    change.setProductName(rs.getString("product_name"));
                    changes.add(change);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return changes;
    }
    
    @Override
    public List<InventoryChange> getAllInventoryChanges() {
        List<InventoryChange> changes = new ArrayList<>();
        String sql = "SELECT ic.*, p.product_name FROM inventory_changes ic " +
                    "LEFT JOIN products p ON ic.product_id = p.product_id " +
                    "ORDER BY ic.change_date DESC";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                InventoryChange change = new InventoryChange();
                change.setChangeId(rs.getInt("change_id"));
                change.setProductId(rs.getInt("product_id"));
                change.setChangeType(rs.getString("change_type"));
                change.setQuantity(rs.getInt("quantity"));
                change.setReason(rs.getString("reason"));
                change.setChangeDate(rs.getTimestamp("change_date"));
                change.setOperator(rs.getString("operator"));
                change.setProductName(rs.getString("product_name"));
                changes.add(change);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return changes;
    }
}