package com.example.crud.infrastructure.adapter.output;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.port.output.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final Map<Long, Product> database = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Product save(Product product) {
        Long id = product.id() == null ? idGenerator.getAndIncrement() : product.id();
        Product saved = new Product(id, product.name(), product.price());
        database.put(id, saved);
        return saved;
    }

    @Override
    public Optional<Product> findById(Long id) { return Optional.ofNullable(database.get(id)); }

    @Override
    public List<Product> findAll() { return new ArrayList<>(database.values()); }

    @Override
    public void deleteById(Long id) { database.remove(id); }
}
