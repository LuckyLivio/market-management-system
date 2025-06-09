package com.supermarket.dao;

import com.supermarket.model.Sale;
import java.util.List;
import java.util.Date;

public interface SaleDAO {
    boolean addSale(Sale sale);
    List<Sale> getAllSales();
    List<Sale> getSalesByDateRange(Date startDate, Date endDate);
    Sale getSaleById(int saleId);
    double getTotalSalesAmount(Date startDate, Date endDate);
    List<Sale> getSalesByPage(int page, int pageSize);
    int getTotalSalesCount();
}