package com.supermarket.model;

import java.math.BigDecimal;

/**
 * 销售明细实体类
 */
public class SaleDetail {
    private Integer detailId;           // 明细ID
    private String receiptId;           // 票号
    private String productId;           // 商品号
    private String productName;         // 商品名（冗余字段，便于显示）
    private Integer quantity;           // 数量
    private BigDecimal unitPrice;       // 单价
    private BigDecimal subtotal;        // 小计
    
    // 无参构造函数
    public SaleDetail() {}
    
    // 构造函数
    public SaleDetail(String receiptId, String productId, Integer quantity, 
                      BigDecimal unitPrice, BigDecimal subtotal) {
        this.receiptId = receiptId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
    
    // Getter和Setter方法
    public Integer getDetailId() {
        return detailId;
    }
    
    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }
    
    public String getReceiptId() {
        return receiptId;
    }
    
    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }
    
    public String getProductId() {
        return productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}