package com.example.crud.application.port.input;

import java.util.Optional;

/**
 * Application port for authentication operations.
 * This interface defines what the application offers in terms of user authentication.
 */
public interface AuthService {
    Optional<String> authenticateAndGenerateToken(String username, String password);
}
