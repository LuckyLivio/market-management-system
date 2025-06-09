package com.supermarket.service.impl;

import com.supermarket.dao.ProductDAO;
import com.supermarket.dao.impl.ProductDAOImpl;
import com.supermarket.model.Product;
import com.supermarket.service.ProductService;

import java.util.List;

/**
 * 商品业务逻辑实现类
 */
public class ProductServiceImpl implements ProductService {
    
    private ProductDAO productDAO;
    
    public ProductServiceImpl() {
        this.productDAO = new ProductDAOImpl();
    }
    
    @Override
    public boolean addProduct(Product product) {
        // 业务逻辑验证
        if (product == null || product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            return false;
        }
        
        // 检查商品是否已存在
        if (isProductExists(product.getProductId())) {
            return false;
        }
        
        return productDAO.addProduct(product);
    }
    
    @Override
    public boolean deleteProduct(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return false;
        }
        
        return productDAO.deleteProduct(productId);
    }
    
    @Override
    public boolean updateProduct(Product product) {
        if (product == null || product.getProductId() == null) {
            return false;
        }
        
        return productDAO.updateProduct(product);
    }
    
    @Override
    public Product getProductById(String productId) {
        if (productId == null || productId.trim().isEmpty()) {
            return null;
        }
        
        return productDAO.getProductById(productId);
    }
    
    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    @Override
    public List<Product> searchProductsByName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return getAllProducts();
        }
        
        return productDAO.getProductsByName(productName.trim());
    }
    
    @Override
    public List<Product> getLowStockProducts() {
        return productDAO.getLowStockProducts();
    }
    
    @Override
    public boolean stockIn(String productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return false;
        }
        
        return productDAO.updateStock(productId, quantity);
    }
    
    @Override
    public boolean stockOut(String productId, int quantity) {
        if (productId == null || quantity <= 0) {
            return false;
        }
        
        // 检查库存是否充足
        if (!isStockSufficient(productId, quantity)) {
            return false;
        }
        
        return productDAO.updateStock(productId, -quantity);
    }
    
    @Override
    public boolean isProductExists(String productId) {
        return getProductById(productId) != null;
    }
    
    @Override
    public boolean isStockSufficient(String productId, int quantity) {
        Product product = getProductById(productId);
        return product != null && product.getStockQuantity() >= quantity;
    }
}