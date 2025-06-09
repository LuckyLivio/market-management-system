package com.supermarket.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * 销售记录实体类
 */
public class Sale {
    private String receiptId;           // 票号
    private Date saleDate;              // 销售日期
    private Time saleTime;              // 销售时间
    private String cashierId;           // 收银台
    private String memberId;            // 会员号
    private BigDecimal totalAmount;     // 总金额
    private BigDecimal discount;        // 折扣
    private BigDecimal cashReceived;    // 现金
    private BigDecimal changeAmount;    // 找零
    private Timestamp createdAt;        // 创建时间
    private List<SaleDetail> saleDetails; // 销售明细列表
    
    // 无参构造函数
    public Sale() {}
    
    // 构造函数
    public Sale(String receiptId, Date saleDate, Time saleTime, String cashierId, 
                String memberId, BigDecimal totalAmount, BigDecimal discount, 
                BigDecimal cashReceived, BigDecimal changeAmount) {
        this.receiptId = receiptId;
        this.saleDate = saleDate;
        this.saleTime = saleTime;
        this.cashierId = cashierId;
        this.memberId = memberId;
        this.totalAmount = totalAmount;
        this.discount = discount;
        this.cashReceived = cashReceived;
        this.changeAmount = changeAmount;
    }
    
    // Getter和Setter方法
    public String getReceiptId() {
        return receiptId;
    }
    
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    
    public Date getSaleDate() {
        return saleDate;
    }
    
    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
    
    public Time getSaleTime() {
        return saleTime;
    }
    
    public void setSaleTime(Time saleTime) {
        this.saleTime = saleTime;
    }
    
    public String getCashierId() {
        return cashierId;
    }
    
    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }
    
    public String getMemberId() {
        return memberId;
    }
    
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getDiscount() {
        return discount;
    }
    
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    
    public BigDecimal getCashReceived() {
        return cashReceived;
    }
    
    public void setCashReceived(BigDecimal cashReceived) {
        this.cashReceived = cashReceived;
    }
    
    public BigDecimal getChangeAmount() {
        return changeAmount;
    }
    
    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public List<SaleDetail> getSaleDetails() {
        return saleDetails;
    }
    
    public void setSaleDetails(List<SaleDetail> saleDetails) {
        this.saleDetails = saleDetails;
    }
}