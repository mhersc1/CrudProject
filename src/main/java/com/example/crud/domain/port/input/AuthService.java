package com.example.crud.domain.port.input;

import java.util.Optional;

public interface AuthService {
    Optional<String> authenticateAndGenerateToken(String username, String password);
}
