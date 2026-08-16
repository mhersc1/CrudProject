package com.example.crud.application.usecase;

import com.example.crud.application.port.input.ProductQueryService;
import com.example.crud.application.port.input.ProductStatisticsService;
import com.example.crud.application.port.output.ProductRepository;
import com.example.crud.domain.exception.ProductNotFoundException;
import com.example.crud.domain.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GraphQL-specific use case for advanced product operations.
 * This class implements all complex querying, filtering, sorting, and statistics operations.
 * It directly uses the repository for efficient data access without delegation through service ports.
 */
@ApplicationScoped
public class GraphQLProductUseCase implements ProductQueryService, ProductStatisticsService {
    
    private final ProductRepository repository;
    
    public GraphQLProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }
    
    /**
     * Retrieves products with filtering, sorting, and pagination.
     * Returns the raw product list along with the total count for GraphQL pagination.
     * 
     * @param filter Filter criteria (name contains, price range)
     * @param sort Sort specification (field and direction)
     * @param pagination Pagination parameters (limit and offset)
     * @return QueryResults containing filtered products and total count
     */
    public QueryResults getProductsWithFilter(
        ProductFilter filter, 
        ProductSort sort, 
        Pagination pagination
    ) {
        // Get all products for filtering
        List<Product> allProducts = repository.findAll();
        
        // Apply filtering
        if (filter != null) {
            allProducts = allProducts.stream()
                .filter(product -> {
                    if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                        if (!product.name().toLowerCase().contains(filter.getNameContains().toLowerCase())) {
                            return false;
                        }
                    }
                    if (filter.getPriceMin() != null) {
                        if (product.price() < filter.getPriceMin()) {
                            return false;
                        }
                    }
                    if (filter.getPriceMax() != null) {
                        if (product.price() > filter.getPriceMax()) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        }
        
        // Get total count before pagination
        int totalCount = allProducts.size();
        
        // Apply sorting
        if (sort != null && sort.getField() != null) {
            Comparator<Product> comparator = switch (sort.getField()) {
                case NAME -> Comparator.comparing(Product::name);
                case PRICE -> Comparator.comparing(Product::price);
                case ID -> Comparator.comparing(Product::id);
            };
            
            if (sort.getDirection() == SortDirection.DESC) {
                comparator = comparator.reversed();
            }
            
            allProducts = allProducts.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
        }
        
        // Apply pagination
        List<Product> paginatedProducts = allProducts;
        if (pagination != null) {
            paginatedProducts = allProducts.stream()
                .skip(pagination.getOffset())
                .limit(pagination.getLimit())
                .collect(Collectors.toList());
        }
        
        return new QueryResults(paginatedProducts, totalCount);
    }
    
    // Implementation of ProductQueryService interface
    
    @Override
    public List<Product> filterProducts(ProductFilter filter, ProductSort sort, Pagination pagination) {
        QueryResults results = getProductsWithFilter(filter, sort, pagination);
        return results.getProducts();
    }
    
    @Override
    public int countProducts(ProductFilter filter) {
        List<Product> allProducts = repository.findAll();
        
        if (filter != null) {
            allProducts = allProducts.stream()
                .filter(product -> {
                    if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                        if (!product.name().toLowerCase().contains(filter.getNameContains().toLowerCase())) {
                            return false;
                        }
                    }
                    if (filter.getPriceMin() != null) {
                        if (product.price() < filter.getPriceMin()) {
                            return false;
                        }
                    }
                    if (filter.getPriceMax() != null) {
                        if (product.price() > filter.getPriceMax()) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        }
        
        return allProducts.size();
    }
    
    // Implementation of ProductStatisticsService interface
    
    @Override
    public ProductStats calculateStatistics(ProductFilter filter) {
        List<Product> products = repository.findAll();
        
        // Apply filtering if provided
        if (filter != null) {
            products = products.stream()
                .filter(product -> {
                    if (filter.getNameContains() != null && !filter.getNameContains().isEmpty()) {
                        if (!product.name().toLowerCase().contains(filter.getNameContains().toLowerCase())) {
                            return false;
                        }
                    }
                    if (filter.getPriceMin() != null) {
                        if (product.price() < filter.getPriceMin()) {
                            return false;
                        }
                    }
                    if (filter.getPriceMax() != null) {
                        if (product.price() > filter.getPriceMax()) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
        }
        
        int count = products.size();
        double averagePrice = products.isEmpty() ? 0.0 : 
            products.stream().mapToDouble(Product::price).average().orElse(0.0);
        double minPrice = products.isEmpty() ? 0.0 : 
            products.stream().mapToDouble(Product::price).min().orElse(0.0);
        double maxPrice = products.isEmpty() ? 0.0 : 
            products.stream().mapToDouble(Product::price).max().orElse(0.0);
        
        return new ProductStats(count, averagePrice, minPrice, maxPrice);
    }
    
    /**
     * Retrieves product statistics directly for GraphQL consumption.
     * 
     * @param filter Filter criteria for statistics calculation
     * @return Product statistics including count, average price, min price, max price
     */
    public ProductStats getProductStatistics(ProductFilter filter) {
        return calculateStatistics(filter);
    }
    
    /**
     * Helper class to hold query results with pagination metadata.
     * This is a lightweight container for GraphQL use case operations.
     */
    public static class QueryResults {
        private final List<Product> products;
        private final int totalCount;
        
        public QueryResults(List<Product> products, int totalCount) {
            this.products = products;
            this.totalCount = totalCount;
        }
        
        public List<Product> getProducts() {
            return products;
        }
        
        public int getTotalCount() {
            return totalCount;
        }
    }
}
