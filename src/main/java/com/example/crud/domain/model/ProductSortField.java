package com.example.crud.domain.model;

/**
 * Represents the fields available for sorting products.
 * This is a domain concept defining the sortable attributes of products.
 */
public enum ProductSortField {
    /**
     * Sort by product name alphabetically
     */
    NAME,
    
    /**
     * Sort by product price numerically
     */
    PRICE,
    
    /**
     * Sort by product ID numerically
     */
    ID
}