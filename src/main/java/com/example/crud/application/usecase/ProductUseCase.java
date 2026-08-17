package com.example.crud.application.usecase;

import com.example.crud.application.port.input.ProductService;
import com.example.crud.application.port.output.ProductRepository;
import com.example.crud.domain.exception.ProductNotFoundException;
import com.example.crud.domain.model.Product;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.NoSuchElementException;

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
            throw new RuntimeException("Error creating a new product");
        }
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException { 
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() { 
        return repository.findAll(); 
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductNotFoundException {
        try {
            // Use the dedicated update method that preserves existing ID
            return repository.updateExisting(id, product);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException(id);
        }
    }

    @Override
    public void removeProduct(Long id) { 
        repository.deleteById(id); 
    }
}