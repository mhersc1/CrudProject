package com.example.crud.domain.exception;

/**
 * Domain exception thrown when a product is not found.
 * Now a checked exception to ensure proper propagation in GraphQL.
 */
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(Long id) {
        super("Product with ID " + id + " was not found.");
    }
}