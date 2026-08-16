package com.example.crud.infrastructure.adapter.input.graphql;

import com.example.crud.domain.model.Product;

/**
 * GraphQL Relay-style edge wrapper for products.
 * This is an infrastructure adapter that provides GraphQL-compatible connection edges.
 * Each edge contains a cursor (for pagination) and the actual product node.
 */
public class ProductEdge {
    
    private String cursor;
    private Product node;
    
    public ProductEdge() {}
    
    public ProductEdge(String cursor, Product node) {
        this.cursor = cursor;
        this.node = node;
    }
    
    public String getCursor() {
        return cursor;
    }
    
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
    
    public Product getNode() {
        return node;
    }
    
    public void setNode(Product node) {
        this.node = node;
    }
}