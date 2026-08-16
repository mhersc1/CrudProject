package com.example.crud.domain.model;

import jakarta.validation.constraints.Min;

/**
 * Represents filtering criteria for product queries.
 * This is a domain concept for filtering product results.
 */
public class ProductFilter {
    
    /**
     * Filter products by name containing this string (case-insensitive)
     */
    private String nameContains;
    
    /**
     * Minimum price filter (inclusive)
     */
    @Min(value = 0, message = "Minimum price must be non-negative")
    private Double priceMin;
    
    /**
     * Maximum price filter (inclusive)
     */
    @Min(value = 0, message = "Maximum price must be non-negative")
    private Double priceMax;
    
    /**
     * Default constructor with no filters.
     */
    public ProductFilter() {}
    
    /**
     * Constructor with explicit filter parameters.
     * 
     * @param nameContains Filter products by name containing this string
     * @param priceMin Minimum price filter
     * @param priceMax Maximum price filter
     */
    public ProductFilter(String nameContains, Double priceMin, Double priceMax) {
        this.nameContains = nameContains;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
    }
    
    public String getNameContains() {
        return nameContains;
    }
    
    public void setNameContains(String nameContains) {
        this.nameContains = nameContains;
    }
    
    public Double getPriceMin() {
        return priceMin;
    }
    
    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }
    
    public Double getPriceMax() {
        return priceMax;
    }
    
    public void setPriceMax(Double priceMax) {
        this.priceMax = priceMax;
    }
}