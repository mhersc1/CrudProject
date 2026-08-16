package com.example.crud.domain.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Represents pagination parameters for querying large datasets.
 * This is a domain concept for controlling result set size and position.
 */
public class Pagination {
    
    @Min(value = 0, message = "Offset must be non-negative")
    private Integer offset = 0;
    
    @Min(value = 1, message = "Limit must be at least 1")
    @Max(value = 100, message = "Limit cannot exceed 100")
    private Integer limit = 20;
    
    /**
     * Default constructor with sensible defaults.
     */
    public Pagination() {}
    
    /**
     * Constructor with explicit pagination parameters.
     * 
     * @param offset The number of items to skip (0-based)
     * @param limit The maximum number of items to return
     */
    public Pagination(Integer offset, Integer limit) {
        this.offset = offset != null ? offset : 0;
        this.limit = limit != null ? limit : 20;
    }
    
    public Integer getOffset() {
        return offset;
    }
    
    public void setOffset(Integer offset) {
        this.offset = offset != null ? offset : 0;
    }
    
    public Integer getLimit() {
        return limit;
    }
    
    public void setLimit(Integer limit) {
        this.limit = limit != null ? limit : 20;
    }
}