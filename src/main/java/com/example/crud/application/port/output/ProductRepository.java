package com.example.crud.application.port.output;

import com.example.crud.domain.model.Product;
import java.util.List;
import java.util.Optional;

/**
 * Application port for product data access.
 * This interface defines what the application needs in terms of product data storage.
 */
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
}
