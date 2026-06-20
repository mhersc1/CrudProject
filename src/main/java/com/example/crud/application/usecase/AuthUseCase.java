package com.example.crud.application.usecase;

import com.example.crud.domain.port.input.AuthService;
import com.example.crud.domain.port.output.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class AuthUseCase implements AuthService {

    private final UserRepository userRepository;

    public AuthUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public Optional<String> authenticateAndGenerateToken(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.password().equals(password)) // In real systems, match hashed values!
                .map(user -> Jwt.issuer("https://example.com/issuer")
                        .upn(user.username())
                        .groups(user.roles())
                        .expiresIn(3600) // 1 Hour Validity
                        .sign());
    }
}
