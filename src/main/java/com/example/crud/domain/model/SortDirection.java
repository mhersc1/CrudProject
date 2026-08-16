package com.example.crud.domain.model;

/**
 * Represents the direction of sorting operations.
 * This is a domain concept that can be reused across different interfaces and use cases.
 */
public enum SortDirection {
    /**
     * Ascending order - lowest to highest
     */
    ASC,
    
    /**
     * Descending order - highest to lowest  
     */
    DESC
}