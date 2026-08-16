package com.example.crud.domain.model;

/**
 * Represents sorting specification for product queries.
 * This is a domain concept defining how results should be ordered.
 */
public class ProductSort {
    
    private ProductSortField field;
    
    private SortDirection direction;
    
    /**
     * Default constructor.
     */
    public ProductSort() {}
    
    /**
     * Constructor with explicit sort parameters.
     * 
     * @param field The field to sort by
     * @param direction The direction of sorting
     */
    public ProductSort(ProductSortField field, SortDirection direction) {
        this.field = field;
        this.direction = direction;
    }
    
    public ProductSortField getField() {
        return field;
    }
    
    public void setField(ProductSortField field) {
        this.field = field;
    }
    
    public SortDirection getDirection() {
        return direction;
    }
    
    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }
}