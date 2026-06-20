package com.example.crud.infrastructure.adapter.input;
import com.example.crud.application.usecase.AuthUseCase;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Path("/api/auth")
@SecurityRequirement(name = "basicAuth")
public class AuthResource {

    private final AuthUseCase authUseCase;

    public AuthResource(AuthUseCase authUseCase) {
        this.authUseCase = authUseCase;
    }

    @POST
    @Path("/token")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAccessToken(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Missing or invalid Authorization header.").build();
        }

        try {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2);

            if (values.length != 2) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Invalid Header components.").build();
            }

            return authUseCase.authenticateAndGenerateToken(values[0], values[1])
                    .map(token -> Response.ok(token).build())
                    .orElseGet(() -> Response.status(Response.Status.UNAUTHORIZED).entity("Bad credentials.").build());

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Malformed Base64 authorization input.").build();
        }
    }
}