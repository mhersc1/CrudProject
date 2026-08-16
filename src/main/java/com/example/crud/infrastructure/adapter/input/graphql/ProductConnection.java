package com.example.crud.infrastructure.adapter.input.graphql;

import com.example.crud.domain.model.Pagination;
import com.example.crud.domain.model.Product;
import java.util.List;

/**
 * GraphQL Relay-style connection for products.
 * This is an infrastructure adapter that provides GraphQL-compatible connection results.
 * Contains edges, pagination metadata, and total count for efficient GraphQL queries.
 */
public class ProductConnection {
    
    private List<ProductEdge> edges;
    private PageInfo pageInfo;
    private Integer totalCount;
    
    public ProductConnection() {}
    
    public ProductConnection(List<ProductEdge> edges, PageInfo pageInfo, Integer totalCount) {
        this.edges = edges;
        this.pageInfo = pageInfo;
        this.totalCount = totalCount;
    }
    
    /**
     * Factory method to create a ProductConnection from domain products.
     * Converts domain pagination results into GraphQL-compatible connection format.
     * 
     * @param products Filtered and paginated product list
     * @param totalCount Total count of products matching the filter
     * @param pagination Domain pagination parameters
     * @return GraphQL-compatible ProductConnection
     */
    public static ProductConnection fromProducts(List<Product> products, Integer totalCount, Pagination pagination) {
        List<ProductEdge> edges = products.stream()
            .map(product -> new ProductEdge(String.valueOf(product.id()), product))
            .toList();
        
        boolean hasNextPage = (pagination.getOffset() + products.size()) < totalCount;
        boolean hasPreviousPage = pagination.getOffset() > 0;
        
        PageInfo pageInfo = new PageInfo(hasNextPage, hasPreviousPage);
        
        return new ProductConnection(edges, pageInfo, totalCount);
    }
    
    public List<ProductEdge> getEdges() {
        return edges;
    }
    
    public void setEdges(List<ProductEdge> edges) {
        this.edges = edges;
    }
    
    public PageInfo getPageInfo() {
        return pageInfo;
    }
    
    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}