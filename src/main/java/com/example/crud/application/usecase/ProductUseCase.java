package com.example.crud.application.usecase;

import com.example.crud.domain.exception.ProductNotFoundException;
import com.example.crud.domain.model.Product;
import com.example.crud.domain.port.input.ProductService;
import com.example.crud.domain.port.output.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

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
    public List<Product> getAllProducts() { return repository.findAll(); }

    @Override
    public void removeProduct(Long id) { repository.deleteById(id); }
}
