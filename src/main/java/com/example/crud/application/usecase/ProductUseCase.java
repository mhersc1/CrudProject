package com.example.crud.application.usecase;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.port.input.ProductService;
import com.example.crud.domain.port.output.ProductRepository;
import java.util.List;

public class ProductUseCase implements ProductService {
    private final ProductRepository repository;

    public ProductUseCase(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product createProduct(Product product) { return repository.save(product); }

    @Override
    public Product getProduct(Long id) { 
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id)); 
    }

    @Override
    public List<Product> getAllProducts() { return repository.findAll(); }

    @Override
    public void removeProduct(Long id) { repository.deleteById(id); }
}
