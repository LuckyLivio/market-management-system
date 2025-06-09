package com.supermarket.service;

import com.supermarket.model.FundOccupation;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 资金占用业务逻辑接口
 */
public interface FundOccupationService {
    
    /**
     * 添加资金占用记录
     */
    boolean addFundOccupation(FundOccupation fundOccupation);
    
    /**
     * 根据ID获取资金占用记录
     */
    FundOccupation getFundOccupationById(int recordId);
    
    /**
     * 获取所有资金占用记录
     */
    List<FundOccupation> getAllFundOccupations();
    
    /**
     * 根据日期范围获取资金占用记录
     */
    List<FundOccupation> getFundOccupationsByDateRange(Date startDate, Date endDate);
    
    /**
     * 根据商品类型获取资金占用记录
     */
    List<FundOccupation> getFundOccupationsByCategory(String category);
    
    /**
     * 更新资金占用记录
     */
    boolean updateFundOccupation(FundOccupation fundOccupation);
    
    /**
     * 删除资金占用记录
     */
    boolean deleteFundOccupation(int recordId);
    
    /**
     * 获取资金占用统计数据
     */
    Map<String, Object> getFundOccupationStatistics(Date startDate, Date endDate);
    
    /**
     * 根据商品类型统计资金占用
     */
    List<Map<String, Object>> getFundOccupationByCategory(Date startDate, Date endDate);
    
    /**
     * 自动计算并生成资金占用记录
     */
    boolean generateFundOccupationRecord(Date statisticsDate, String accountant);
    
    /**
     * 获取可用的商品类型列表
     */
    List<String> getAvailableCategories();
}