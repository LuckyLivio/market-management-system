package com.supermarket.service;

import com.supermarket.model.Sale;
import com.supermarket.model.SaleDetail;
import java.util.Date;
import java.util.List;

public interface SaleService {
    boolean processSale(Sale sale, List<SaleDetail> saleDetails);
    List<Sale> getAllSales();
    List<Sale> getSalesByDateRange(Date startDate, Date endDate);
    Sale getSaleById(int saleId);
    List<SaleDetail> getSaleDetails(int saleId);
    double getTotalSalesAmount(Date startDate, Date endDate);
    List<Sale> getSalesByPage(int page, int pageSize);
    int getTotalSalesCount();
}