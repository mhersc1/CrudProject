package com.example.crud.domain.port.output;

import com.example.crud.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}