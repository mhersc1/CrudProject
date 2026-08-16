package com.example.crud.domain.model;

/**
 * Represents aggregated statistics about products.
 * This is a domain concept for analytics and reporting.
 */
public class ProductStats {
    
    /**
     * Total count of products
     */
    private Integer count;
    
    /**
     * Average price of all products
     */
    private Double averagePrice;
    
    /**
     * Minimum price among all products
     */
    private Double minPrice;
    
    /**
     * Maximum price among all products
     */
    private Double maxPrice;
    
    /**
     * Default constructor.
     */
    public ProductStats() {}
    
    /**
     * Constructor with explicit statistics values.
     * 
     * @param count Total product count
     * @param averagePrice Average product price
     * @param minPrice Minimum product price
     * @param maxPrice Maximum product price
     */
    public ProductStats(Integer count, Double averagePrice, Double minPrice, Double maxPrice) {
        this.count = count;
        this.averagePrice = averagePrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Double getAveragePrice() {
        return averagePrice;
    }
    
    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }
    
    public Double getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    
    public Double getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
}