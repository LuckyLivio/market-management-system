package com.supermarket.service;

import com.supermarket.model.InventoryChange;
import com.supermarket.model.Product;
import java.util.Date;
import java.util.List;

public interface InventoryService {
    boolean adjustInventory(int productId, int quantity, String reason, String operator);
    boolean inStock(int productId, int quantity, String reason, String operator);
    boolean outStock(int productId, int quantity, String reason, String operator);
    List<InventoryChange> getInventoryHistory(int productId);
    List<InventoryChange> getInventoryChangesByDateRange(Date startDate, Date endDate);
    List<Product> getLowStockProducts(int threshold);
    List<InventoryChange> getAllInventoryChanges();
}