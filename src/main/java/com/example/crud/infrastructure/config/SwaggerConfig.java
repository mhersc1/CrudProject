package com.example.crud.infrastructure.config;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
        info = @Info(title = "Product Management API", version = "1.0.0")
)
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = "jwtAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                description = "Provide your valid generated JWT token down below to call CRUD endpoints"
        ),
        @SecurityScheme(
                securitySchemeName = "basicAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "basic",
                description = "Provide credentials (admin/admin123 or alice/password) here to generate a Token"
        )
})
public class SwaggerConfig extends Application {
}