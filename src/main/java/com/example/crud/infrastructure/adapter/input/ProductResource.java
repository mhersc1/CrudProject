package com.example.crud.infrastructure.adapter.input;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.port.input.ProductService;
import jakarta.annotation.security.RolesAllowed;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwtAuth")
@Tag(name = "Product Resource", description = "Product management endpoints using Hexagonal Architecture")
public class ProductResource {
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @RolesAllowed("Admin")
    @Operation(summary = "Create a product", description = "Stores a new product inside the in-memory H2 SQL database")
    public Product create(Product product) { return productService.createProduct(product); }

    @GET
    @Path("/{id}")
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "Find product by ID", description = "Retrieves a single product details from H2 storage")
    public Product get(@PathParam("id") Long id) { return productService.getProduct(id); }

    @GET
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "List all products", description = "Returns a comprehensive list of items available in the database")
    public List<Product> getAll() { return productService.getAllProducts(); }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete a product", description = "Permanently drops a product record by its database primary key")
    public void delete(@PathParam("id") Long id) { productService.removeProduct(id); }
}
