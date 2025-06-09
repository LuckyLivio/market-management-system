package com.supermarket.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * 商品实体类
 */
public class Product {
    private String productId;        // 商品号
    private String productName;      // 商品名
    private String manufacturer;     // 生产厂家
    private BigDecimal unitPrice;    // 单价
    private Integer stockQuantity;   // 库存量
    private Integer minStock;        // 最小库存量
    private Date stockInDate;        // 入库日期
    private Timestamp createdAt;     // 创建时间
    private Timestamp updatedAt;     // 更新时间
    
    // 无参构造函数
    public Product() {}
    
    // 全参构造函数
    public Product(String productId, String productName, String manufacturer, 
                   BigDecimal unitPrice, Integer stockQuantity, Integer minStock, Date stockInDate) {
        this.productId = productId;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.stockQuantity = stockQuantity;
        this.minStock = minStock;
        this.stockInDate = stockInDate;
    }
    
    // Getter和Setter方法
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
    
    public String getManufacturer() {
        return manufacturer;
    }
    
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public Integer getStockQuantity() {
        return stockQuantity;
    }
    
    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public Integer getMinStock() {
        return minStock;
    }
    
    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }
    
    public Date getStockInDate() {
        return stockInDate;
    }
    
    public void setStockInDate(Date stockInDate) {
        this.stockInDate = stockInDate;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", unitPrice=" + unitPrice +
                ", stockQuantity=" + stockQuantity +
                ", minStock=" + minStock +
                ", stockInDate=" + stockInDate +
                '}';
    }
}