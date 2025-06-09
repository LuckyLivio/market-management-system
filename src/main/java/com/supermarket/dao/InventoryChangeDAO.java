package com.supermarket.dao;

import com.supermarket.model.InventoryChange;
import java.util.List;
import java.util.Date;

public interface InventoryChangeDAO {
    boolean addInventoryChange(InventoryChange change);
    List<InventoryChange> getInventoryChangesByProductId(int productId);
    List<InventoryChange> getInventoryChangesByDateRange(Date startDate, Date endDate);
    List<InventoryChange> getAllInventoryChanges();
}