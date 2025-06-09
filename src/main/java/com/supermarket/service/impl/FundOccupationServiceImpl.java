package com.supermarket.service.impl;

import com.supermarket.dao.FundOccupationDAO;
import com.supermarket.dao.ProductDAO;
import com.supermarket.dao.impl.FundOccupationDAOImpl;
import com.supermarket.dao.impl.ProductDAOImpl;
import com.supermarket.model.FundOccupation;
import com.supermarket.model.Product;
import com.supermarket.service.FundOccupationService;
import com.supermarket.util.DBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;

/**
 * 资金占用业务逻辑实现类
 */
public class FundOccupationServiceImpl implements FundOccupationService {
    
    private FundOccupationDAO fundOccupationDAO;
    private ProductDAO productDAO;
    
    public FundOccupationServiceImpl() {
        this.fundOccupationDAO = new FundOccupationDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }
    
    @Override
    public boolean addFundOccupation(FundOccupation fundOccupation) {
        if (fundOccupation == null || fundOccupation.getOccupiedAmount() == null || 
            fundOccupation.getOccupiedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // 计算占用比例
        if (fundOccupation.getTotalOccupied() != null && 
            fundOccupation.getTotalOccupied().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = fundOccupation.getOccupiedAmount()
                .divide(fundOccupation.getTotalOccupied(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
            fundOccupation.setOccupationRatio(ratio);
        }
        
        return fundOccupationDAO.addFundOccupation(fundOccupation);
    }
    
    @Override
    public FundOccupation getFundOccupationById(int recordId) {
        if (recordId <= 0) {
            return null;
        }
        return fundOccupationDAO.getFundOccupationById(recordId);
    }
    
    @Override
    public List<FundOccupation> getAllFundOccupations() {
        return fundOccupationDAO.getAllFundOccupations();
    }
    
    @Override
    public List<FundOccupation> getFundOccupationsByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return new ArrayList<>();
        }
        return fundOccupationDAO.getFundOccupationsByDateRange(startDate, endDate);
    }
    
    @Override
    public List<FundOccupation> getFundOccupationsByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return fundOccupationDAO.getFundOccupationsByCategory(category.trim());
    }
    
    @Override
    public boolean updateFundOccupation(FundOccupation fundOccupation) {
        if (fundOccupation == null || fundOccupation.getRecordId() == null || 
            fundOccupation.getRecordId() <= 0) {
            return false;
        }
        
        // 重新计算占用比例
        if (fundOccupation.getTotalOccupied() != null && 
            fundOccupation.getTotalOccupied().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal ratio = fundOccupation.getOccupiedAmount()
                .divide(fundOccupation.getTotalOccupied(), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
            fundOccupation.setOccupationRatio(ratio);
        }
        
        return fundOccupationDAO.updateFundOccupation(fundOccupation);
    }
    
    @Override
    public boolean deleteFundOccupation(int recordId) {
        if (recordId <= 0) {
            return false;
        }
        return fundOccupationDAO.deleteFundOccupation(recordId);
    }
    
    @Override
    public Map<String, Object> getFundOccupationStatistics(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return new HashMap<>();
        }
        return fundOccupationDAO.getFundOccupationStatistics(startDate, endDate);
    }
    
    @Override
    public List<Map<String, Object>> getFundOccupationByCategory(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return new ArrayList<>();
        }
        return fundOccupationDAO.getFundOccupationByCategory(startDate, endDate);
    }
    
    @Override
    public boolean generateFundOccupationRecord(Date statisticsDate, String accountant) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            
            // 获取所有产品信息
            List<Product> products = productDAO.getAllProducts();
            if (products.isEmpty()) {
                conn.rollback();
                return false;
            }
            
            // 按生产厂家分组计算资金占用
            Map<String, BigDecimal> categoryOccupation = new HashMap<>();
            BigDecimal totalOccupied = BigDecimal.ZERO;
            
            for (Product product : products) {
                BigDecimal productValue = product.getUnitPrice()
                    .multiply(new BigDecimal(product.getStockQuantity()));
                totalOccupied = totalOccupied.add(productValue);
                
                String category = product.getManufacturer() != null ? 
                    product.getManufacturer() : "未分类";
                categoryOccupation.put(category, 
                    categoryOccupation.getOrDefault(category, BigDecimal.ZERO).add(productValue));
            }
            
            // 为每个类别创建资金占用记录
            for (Map.Entry<String, BigDecimal> entry : categoryOccupation.entrySet()) {
                FundOccupation fundOccupation = new FundOccupation();
                fundOccupation.setProductCategory(entry.getKey());
                fundOccupation.setOccupiedAmount(entry.getValue());
                fundOccupation.setTotalOccupied(totalOccupied);
                fundOccupation.setStatisticsDate(statisticsDate);
                fundOccupation.setAccountant(accountant);
                
                // 计算占用比例
                if (totalOccupied.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal ratio = entry.getValue()
                        .divide(totalOccupied, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"));
                    fundOccupation.setOccupationRatio(ratio);
                }
                
                if (!fundOccupationDAO.addFundOccupation(fundOccupation)) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit();
            return true;
        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
        return false;
    }
    
    @Override
    public List<String> getAvailableCategories() {
        List<Product> products = productDAO.getAllProducts();
        Set<String> categories = new HashSet<>();
        
        for (Product product : products) {
            String category = product.getManufacturer() != null ? 
                product.getManufacturer() : "未分类";
            categories.add(category);
        }
        
        return new ArrayList<>(categories);
    }
}