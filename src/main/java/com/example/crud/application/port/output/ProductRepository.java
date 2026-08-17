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
    
    /**
     * Updates an existing product with new values.
     * This method preserves the existing product ID.
     * Different from save() which creates new products with auto-incremental IDs.
     * 
     * @param id The existing product ID to update
     * @param product The product data with new name and price values
     * @return The updated product with the original ID
     * @throws NoSuchElementException if product with the given ID doesn't exist
     */
    Product updateExisting(Long id, Product product);
}
