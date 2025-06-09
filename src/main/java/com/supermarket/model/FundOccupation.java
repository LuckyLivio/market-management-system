package com.supermarket.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 资金占用实体类
 */
public class FundOccupation {
    private Integer recordId;              // 单号
    private String productCategory;        // 商品类型
    private BigDecimal occupiedAmount;     // 占用金额
    private BigDecimal totalOccupied;      // 总占用金额
    private BigDecimal occupationRatio;    // 占用比例
    private Date statisticsDate;           // 统计日期
    private String accountant;             // 会计
    private Timestamp createdAt;           // 创建时间
    
    // 无参构造函数
    public FundOccupation() {}
    
    // 构造函数
    public FundOccupation(String productCategory, BigDecimal occupiedAmount, 
                         BigDecimal totalOccupied, BigDecimal occupationRatio, 
                         Date statisticsDate, String accountant) {
        this.productCategory = productCategory;
        this.occupiedAmount = occupiedAmount;
        this.totalOccupied = totalOccupied;
        this.occupationRatio = occupationRatio;
        this.statisticsDate = statisticsDate;
        this.accountant = accountant;
    }
    
    // Getter和Setter方法
    public Integer getRecordId() {
        return recordId;
    }
    
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
    
    public String getProductCategory() {
        return productCategory;
    }
    
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
    
    public BigDecimal getOccupiedAmount() {
        return occupiedAmount;
    }
    
    public void setOccupiedAmount(BigDecimal occupiedAmount) {
        this.occupiedAmount = occupiedAmount;
    }
    
    public BigDecimal getTotalOccupied() {
        return totalOccupied;
    }
    
    public void setTotalOccupied(BigDecimal totalOccupied) {
        this.totalOccupied = totalOccupied;
    }
    
    public BigDecimal getOccupationRatio() {
        return occupationRatio;
    }
    
    public void setOccupationRatio(BigDecimal occupationRatio) {
        this.occupationRatio = occupationRatio;
    }
    
    public Date getStatisticsDate() {
        return statisticsDate;
    }
    
    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }
    
    public String getAccountant() {
        return accountant;
    }
    
    public void setAccountant(String accountant) {
        this.accountant = accountant;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "FundOccupation{" +
                "recordId=" + recordId +
                ", productCategory='" + productCategory + '\'' +
                ", occupiedAmount=" + occupiedAmount +
                ", totalOccupied=" + totalOccupied +
                ", occupationRatio=" + occupationRatio +
                ", statisticsDate=" + statisticsDate +
                ", accountant='" + accountant + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}