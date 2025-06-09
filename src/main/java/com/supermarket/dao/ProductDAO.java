package com.supermarket.dao;

import com.supermarket.model.Product;
import java.util.List;

/**
 * 商品数据访问接口
 */
public interface ProductDAO {
    
    /**
     * 添加商品
     */
    boolean addProduct(Product product);
    
    /**
     * 根据商品号删除商品
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
    List<Product> getProductsByName(String productName);
    
    /**
     * 查询库存不足的商品
     */
    List<Product> getLowStockProducts();
    
    /**
     * 更新商品库存
     */
    boolean updateStock(String productId, int quantity);
}