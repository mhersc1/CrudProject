package com.example.crud.infrastructure.config;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;

public class SecuritySchemeFilter implements OASFilter {

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        if (openAPI.getComponents() != null && openAPI.getComponents().getSecuritySchemes() != null) {
            // Remove the duplicate default auto-generated scheme completely
            openAPI.getComponents().getSecuritySchemes().remove("SecurityScheme");
        }
    }
}