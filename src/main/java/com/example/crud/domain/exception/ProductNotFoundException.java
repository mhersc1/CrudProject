package com.example.crud.domain.exception;

/**
 * Domain exception thrown when a product is not found.
 * Pure domain exception without presentation concerns.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product with ID " + id + " was not found.");
    }
}
