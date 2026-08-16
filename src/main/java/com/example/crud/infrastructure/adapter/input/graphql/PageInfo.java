package com.example.crud.infrastructure.adapter.input.graphql;

/**
 * GraphQL-specific pagination metadata following the Relay Connection specification.
 * This is an infrastructure adapter that provides GraphQL-compatible pagination information.
 * The actual pagination logic is handled by domain models, this just adapts the results for GraphQL.
 */
public class PageInfo {
    
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    
    public PageInfo() {}
    
    public PageInfo(boolean hasNextPage, boolean hasPreviousPage) {
        this.hasNextPage = hasNextPage;
        this.hasPreviousPage = hasPreviousPage;
    }
    
    public boolean isHasNextPage() {
        return hasNextPage;
    }
    
    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
    
    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }
    
    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }
}