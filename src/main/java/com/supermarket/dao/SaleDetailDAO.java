package com.supermarket.dao;

import com.supermarket.model.SaleDetail;
import java.util.List;

public interface SaleDetailDAO {
    boolean addSaleDetail(SaleDetail saleDetail);
    List<SaleDetail> getSaleDetailsBySaleId(int saleId);
    boolean deleteSaleDetailsBySaleId(int saleId);
}