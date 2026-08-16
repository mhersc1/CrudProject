package com.example.crud.application.usecase;

import com.example.crud.application.port.input.ProductService;
import com.example.crud.application.port.output.ProductRepository;
import com.example.crud.domain.exception.ProductNotFoundException;
import com.example.crud.domain.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

/**
 * Use case implementation for basic product CRUD operations.
 * This class focuses only on the core product management functionality.
 */
@ApplicationScoped
public class ProductUseCase implements ProductService {
    private final ProductRepository repository;

    public ProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        try {
            return repository.save(product);
        } catch (Exception e){
            throw new ProductNotFoundException(product.id());
        }
    }

    @Override
    public Product getProduct(Long id) { 
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() { 
        return repository.findAll(); 
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        // First verify the product exists
        Product existing = repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
        
        // Create updated product with the same ID but new values
        Product updated = new Product(existing.id(), product.name(), product.price());
        
        // Save the updated product
        return repository.save(updated);
    }

    @Override
    public void removeProduct(Long id) { 
        repository.deleteById(id); 
    }
}
