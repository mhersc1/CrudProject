package com.example.crud.infrastructure.adapter.output;

import com.example.crud.domain.model.Product;
import com.example.crud.application.port.output.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Pure in-memory implementation of ProductRepository.
 * Uses no database, no persistence, no SQL - just memory storage.
 * This is a true in-memory repository for testing and development.
 */
@ApplicationScoped
public class InMemoryProductRepository implements ProductRepository {
    
    // Thread-safe in-memory storage
    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    
    // Auto-generating ID sequence
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Product save(Product product) {
        // ALWAYS use auto-incremental ID - ignore any user-provided ID
        // This ensures consistent ID generation regardless of user input
        Long id = idGenerator.getAndIncrement();
        
        // Create new product with auto-generated ID (ignore user's ID completely)
        Product newProduct = new Product(id, product.name(), product.price());
        
        // Store in memory
        products.put(id, newProduct);
        
        return newProduct;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }
    
    /**
     * Updates an existing product with new values.
     * This method is specifically designed for updates - it preserves the existing ID.
     * Different from save() which always creates new products with auto-incremental IDs.
     * 
     * @param id The existing product ID to update
     * @param product The product data with new name and price values
     * @return The updated product with the original ID
     * @throws NoSuchElementException if product with the given ID doesn't exist
     */
    public Product updateExisting(Long id, Product product) {
        // Verify product exists before updating
        if (!products.containsKey(id)) {
            throw new NoSuchElementException("Product with ID " + id + " not found");
        }
        
        // Create updated product with the EXISTING ID (not auto-generated)
        Product updatedProduct = new Product(id, product.name(), product.price());
        
        // Replace the existing product in memory
        products.put(id, updatedProduct);
        
        return updatedProduct;
    }
    
    /**
     * Clears all products from memory.
     * Useful for testing to get clean state.
     */
    public void clearAll() {
        products.clear();
        idGenerator.set(1); // Reset ID sequence
    }
    
    /**
     * Returns current count of products in memory.
     */
    public int count() {
        return products.size();
    }
}
