package com.example.crud.infrastructure.adapter.input;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.port.input.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product create(@RequestBody Product product) { return productService.createProduct(product); }

    @GetMapping("/{id}")
    public Product get(@PathVariable Long id) { return productService.getProduct(id); }

    @GetMapping
    public List<Product> getAll() { return productService.getAllProducts(); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { productService.removeProduct(id); }
}
