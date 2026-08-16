package com.example.crud.application.port.input;

import com.example.crud.domain.model.Product;
import java.util.List;

/**
 * Application port for basic product CRUD operations.
 * This interface defines what the application offers in terms of product management.
 */
public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
    Product updateProduct(Long id, Product product);
    void removeProduct(Long id);
}
