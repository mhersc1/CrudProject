package com.example.crud.infrastructure.adapter.output;

import com.example.crud.domain.model.User;
import com.example.crud.domain.port.output.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class InMemoryUserRepository implements UserRepository {
    // In-memory representation acting as a basic database
    private static final Map<String, User> USERS = Map.of(
            "admin", new User("admin", "admin", Set.of("Admin", "User")),
            "mher", new User("mher", "password", Set.of("User"))
    );

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(USERS.get(username));
    }
}
