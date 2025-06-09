package com.supermarket.model;

import java.util.Date;

public class InventoryChange {
    private int changeId;
    private int productId;
    private String changeType; // IN-入库, OUT-出库, ADJUST-调整
    private int quantity;
    private String reason;
    private Date changeDate;
    private String operator;
    private String productName; // 用于显示
    
    // 构造函数
    public InventoryChange() {}
    
    public InventoryChange(int productId, String changeType, int quantity, String reason, String operator) {
        this.productId = productId;
        this.changeType = changeType;
        this.quantity = quantity;
        this.reason = reason;
        this.operator = operator;
        this.changeDate = new Date();
    }
    
    // Getter和Setter方法
    public int getChangeId() {
        return changeId;
    }
    
    public void setChangeId(int changeId) {
        this.changeId = changeId;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public String getChangeType() {
        return changeType;
    }
    
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public Date getChangeDate() {
        return changeDate;
    }
    
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
}