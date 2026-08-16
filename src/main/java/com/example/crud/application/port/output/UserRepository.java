package com.example.crud.application.port.output;

import com.example.crud.domain.model.User;

import java.util.Optional;

/**
 * Application port for user data access.
 * This interface defines what the application needs in terms of user data storage.
 */
public interface UserRepository {
    Optional<User> findByUsername(String username);
}
