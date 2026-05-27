package com.example.crud.infrastructure.adapter.output;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.port.output.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryProductRepository implements ProductRepository { // <-- Removed "implements PanacheRepository"

    @Override
    @Transactional
    public Product save(Product product) {
        // Map domain record to DB entity
        ProductEntity entity = new ProductEntity(product.id(), product.name(), product.price());

        if (entity.getId() == null) {
            entity.persist(); // <-- Call persist directly on the entity instance
        } else {
            entity = ProductEntity.getEntityManager().merge(entity);
        }

        return new Product(entity.getId(), entity.getName(), entity.getPrice());
    }

    @Override
    public Optional<Product> findById(Long id) {
        // <-- Call static PanacheEntityBase method safely
        return ProductEntity.<ProductEntity>findByIdOptional(id)
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice()));
    }

    @Override
    public List<Product> findAll() {
        // <-- Call static listAll() from the entity class
        return ProductEntity.<ProductEntity>listAll().stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // <-- Call static delete from the entity class to bypass signature collision completely
        ProductEntity.delete("id", id);
    }
}