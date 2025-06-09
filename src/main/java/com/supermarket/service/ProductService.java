package com.supermarket.service;

import com.supermarket.model.Product;
import java.util.List;

/**
 * 商品业务逻辑接口
 */
public interface ProductService {
    
    /**
     * 添加商品
     */
    boolean addProduct(Product product);
    
    /**
     * 删除商品
     */
    boolean deleteProduct(String productId);
    
    /**
     * 更新商品信息
     */
    boolean updateProduct(Product product);
    
    /**
     * 根据商品号查询商品
     */
    Product getProductById(String productId);
    
    /**
     * 查询所有商品
     */
    List<Product> getAllProducts();
    
    /**
     * 根据商品名模糊查询
     */
    List<Product> searchProductsByName(String productName);
    
    /**
     * 查询库存不足的商品
     */
    List<Product> getLowStockProducts();
    
    /**
     * 商品入库
     */
    boolean stockIn(String productId, int quantity);
    
    /**
     * 商品出库
     */
    boolean stockOut(String productId, int quantity);
    
    /**
     * 检查商品是否存在
     */
    boolean isProductExists(String productId);
    
    /**
     * 检查库存是否充足
     */
    boolean isStockSufficient(String productId, int quantity);
}