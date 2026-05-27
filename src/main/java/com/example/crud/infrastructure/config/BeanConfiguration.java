package com.example.crud.infrastructure.config;

import com.example.crud.application.usecase.ProductUseCase;
import com.example.crud.domain.port.input.ProductService;
import com.example.crud.domain.port.output.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductUseCase(productRepository);
    }
}
