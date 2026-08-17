package com.example.crud.infrastructure.adapter.input;

import com.example.crud.application.usecase.GraphQLProductUseCase;
import com.example.crud.domain.exception.ProductNotFoundException;
import com.example.crud.domain.model.*;
import com.example.crud.application.port.input.ProductService;
import com.example.crud.infrastructure.adapter.input.graphql.*;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.inject.Inject;
import org.eclipse.microprofile.graphql.*;

import java.util.List;
import java.util.Set;

/**
 * GraphQL resource endpoint for product and user operations.
 * This infrastructure adapter coordinates between GraphQL-specific presentation
 * and domain business logic through use cases.
 * 
 * Follows clean architecture principles:
 * - GraphQL adapters (Connection, Edge, PageInfo) are infrastructure concerns
 * - Domain models (Filter, Sort, Pagination) are in the domain layer
 * - Business logic is orchestrated by use cases in the application layer
 */
@GraphQLApi
public class GraphQLResource {
    
    private final ProductService productService;
    private final GraphQLProductUseCase graphQLProductUseCase;
    
    @Inject
    JsonWebToken jwt;

    public GraphQLResource(
        ProductService productService, 
        GraphQLProductUseCase graphQLProductUseCase
    ) {
        this.productService = productService;
        this.graphQLProductUseCase = graphQLProductUseCase;
    }
    
    // Basic CRUD Queries
    
    @Query
    @RolesAllowed({"User", "Admin"})
    public Product product(@Name("id") Long id) throws ProductNotFoundException {
        return productService.getProduct(id);
    }
    
    @Query
    @RolesAllowed({"User", "Admin"})
    public List<Product> products() {
        return productService.getAllProducts();
    }
    
    // Complex Query with Filtering, Sorting, and Pagination
    
    @Query
    @RolesAllowed({"User", "Admin"})
    public ProductConnection productsWithFilter(
        @Name("filter") ProductFilter filter,
        @Name("sort") ProductSort sort,
        @Name("pagination") Pagination pagination
    ) {
        if (pagination == null) {
            pagination = new Pagination();
        }
        
        // Use the GraphQL use case to handle business logic
        GraphQLProductUseCase.QueryResults results = 
            graphQLProductUseCase.getProductsWithFilter(filter, sort, pagination);
        
        // Convert domain results to GraphQL-specific connection format
        return ProductConnection.fromProducts(results.getProducts(), results.getTotalCount(), pagination);
    }
    
    // Mutations
    
    @Mutation
    @RolesAllowed("Admin")
    public Product createProduct(@Name("input") ProductInput input) throws ProductNotFoundException {
        Product product = new Product(null, input.getName(), input.getPrice());
        return productService.createProduct(product);
    }
    
    @Mutation
    @RolesAllowed("Admin")
    public Product updateProduct(@Name("id") Long id, @Name("input") ProductInput input) throws ProductNotFoundException {
        Product existing = productService.getProduct(id);
        Product updated = new Product(existing.id(), input.getName(), input.getPrice());
        return productService.updateProduct(id, updated);
    }
    
    @Mutation
    @RolesAllowed("Admin")
    public Boolean deleteProduct(@Name("id") Long id) {
        productService.removeProduct(id);
        return true;
    }
    
    // Aggregated Queries
    
    @Query
    @RolesAllowed({"User", "Admin"})
    public ProductStats productStats(@Name("filter") ProductFilter filter) {
        // Use the GraphQL use case to handle business logic
        return graphQLProductUseCase.getProductStatistics(filter);
    }
    
    // Auth Query
    
    @Query
    @RolesAllowed({"User", "Admin"})
    public User me() {
        String username = jwt.getName();
        List<String> roles = jwt.getGroups().stream().toList();
        return new User(username, "[REDACTED]", Set.copyOf(roles));
    }
}