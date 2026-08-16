package com.example.crud.application.port.input;

import com.example.crud.domain.model.ProductFilter;
import com.example.crud.domain.model.ProductStats;

/**
 * Application port for product statistics and analytics.
 * This interface defines what the application offers in terms of product analytics.
 */
public interface ProductStatisticsService {
    
    /**
     * Calculates statistics for products matching the given filter criteria.
     * 
     * @param filter Filter criteria (name contains, price range)
     * @return Statistics including count, average price, min price, max price
     */
    ProductStats calculateStatistics(ProductFilter filter);
}
