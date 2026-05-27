package com.example.crud.domain.port.input;

import com.example.crud.domain.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product getProduct(Long id);
    List<Product> getAllProducts();
    void removeProduct(Long id);
}
