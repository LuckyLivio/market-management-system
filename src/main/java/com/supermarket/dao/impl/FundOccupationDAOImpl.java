package com.supermarket.dao.impl;

import com.supermarket.dao.FundOccupationDAO;
import com.supermarket.model.FundOccupation;
import com.supermarket.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * 资金占用数据访问实现类
 */
public class FundOccupationDAOImpl implements FundOccupationDAO {
    
    @Override
    public boolean addFundOccupation(FundOccupation fundOccupation) {
        String sql = "INSERT INTO fund_occupation (product_category, occupied_amount, total_occupied, occupation_ratio, statistics_date, accountant) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fundOccupation.getProductCategory());
            pstmt.setBigDecimal(2, fundOccupation.getOccupiedAmount());
            pstmt.setBigDecimal(3, fundOccupation.getTotalOccupied());
            pstmt.setBigDecimal(4, fundOccupation.getOccupationRatio());
            pstmt.setDate(5, fundOccupation.getStatisticsDate());
            pstmt.setString(6, fundOccupation.getAccountant());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public FundOccupation getFundOccupationById(int recordId) {
        String sql = "SELECT * FROM fund_occupation WHERE record_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToFundOccupation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<FundOccupation> getAllFundOccupations() {
        String sql = "SELECT * FROM fund_occupation ORDER BY statistics_date DESC, created_at DESC";
        List<FundOccupation> fundOccupations = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                fundOccupations.add(mapResultSetToFundOccupation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fundOccupations;
    }
    
    @Override
    public List<FundOccupation> getFundOccupationsByDateRange(Date startDate, Date endDate) {
        String sql = "SELECT * FROM fund_occupation WHERE statistics_date BETWEEN ? AND ? ORDER BY statistics_date DESC";
        List<FundOccupation> fundOccupations = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                fundOccupations.add(mapResultSetToFundOccupation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fundOccupations;
    }
    
    @Override
    public List<FundOccupation> getFundOccupationsByCategory(String category) {
        String sql = "SELECT * FROM fund_occupation WHERE product_category = ? ORDER BY statistics_date DESC";
        List<FundOccupation> fundOccupations = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                fundOccupations.add(mapResultSetToFundOccupation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fundOccupations;
    }
    
    @Override
    public boolean updateFundOccupation(FundOccupation fundOccupation) {
        String sql = "UPDATE fund_occupation SET product_category = ?, occupied_amount = ?, total_occupied = ?, occupation_ratio = ?, statistics_date = ?, accountant = ? WHERE record_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, fundOccupation.getProductCategory());
            pstmt.setBigDecimal(2, fundOccupation.getOccupiedAmount());
            pstmt.setBigDecimal(3, fundOccupation.getTotalOccupied());
            pstmt.setBigDecimal(4, fundOccupation.getOccupationRatio());
            pstmt.setDate(5, fundOccupation.getStatisticsDate());
            pstmt.setString(6, fundOccupation.getAccountant());
            pstmt.setInt(7, fundOccupation.getRecordId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean deleteFundOccupation(int recordId) {
        String sql = "DELETE FROM fund_occupation WHERE record_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recordId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public Map<String, Object> getFundOccupationStatistics(Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) as record_count, SUM(occupied_amount) as total_occupied, AVG(occupation_ratio) as avg_ratio FROM fund_occupation WHERE statistics_date BETWEEN ? AND ?";
        Map<String, Object> statistics = new HashMap<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                statistics.put("recordCount", rs.getInt("record_count"));
                statistics.put("totalOccupied", rs.getBigDecimal("total_occupied"));
                statistics.put("avgRatio", rs.getBigDecimal("avg_ratio"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistics;
    }
    
    @Override
    public List<Map<String, Object>> getFundOccupationByCategory(Date startDate, Date endDate) {
        String sql = "SELECT product_category, SUM(occupied_amount) as category_occupied, AVG(occupation_ratio) as avg_ratio, COUNT(*) as record_count FROM fund_occupation WHERE statistics_date BETWEEN ? AND ? GROUP BY product_category ORDER BY category_occupied DESC";
        List<Map<String, Object>> categoryStats = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> stat = new HashMap<>();
                stat.put("category", rs.getString("product_category"));
                stat.put("categoryOccupied", rs.getBigDecimal("category_occupied"));
                stat.put("avgRatio", rs.getBigDecimal("avg_ratio"));
                stat.put("recordCount", rs.getInt("record_count"));
                categoryStats.add(stat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryStats;
    }
    
    /**
     * 将ResultSet映射为FundOccupation对象
     */
    private FundOccupation mapResultSetToFundOccupation(ResultSet rs) throws SQLException {
        FundOccupation fundOccupation = new FundOccupation();
        fundOccupation.setRecordId(rs.getInt("record_id"));
        fundOccupation.setProductCategory(rs.getString("product_category"));
        fundOccupation.setOccupiedAmount(rs.getBigDecimal("occupied_amount"));
        fundOccupation.setTotalOccupied(rs.getBigDecimal("total_occupied"));
        fundOccupation.setOccupationRatio(rs.getBigDecimal("occupation_ratio"));
        fundOccupation.setStatisticsDate(rs.getDate("statistics_date"));
        fundOccupation.setAccountant(rs.getString("accountant"));
        fundOccupation.setCreatedAt(rs.getTimestamp("created_at"));
        return fundOccupation;
    }
}