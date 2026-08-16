package com.example.crud.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Represents input data for creating or updating products.
 * This is a domain concept for product mutation operations.
 */
public class ProductInput {
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    @NotNull(message = "Product price is required")
    @Positive(message = "Price must be positive")
    private Double price;
    
    /**
     * Default constructor.
     */
    public ProductInput() {}
    
    /**
     * Constructor with explicit product input values.
     * 
     * @param name Product name
     * @param price Product price
     */
    public ProductInput(String name, Double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
}