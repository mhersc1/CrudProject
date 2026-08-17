package com.example.crud.infrastructure.adapter.input;

import com.example.crud.domain.exception.ProductNotFoundException;
import com.example.crud.domain.model.Product;
import com.example.crud.application.port.input.ProductService;
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
@SecurityRequirement(name = "SecurityScheme")
@Tag(name = "Product Resource", description = "Product management endpoints using Hexagonal Architecture")
public class ProductResource {
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @POST
    @RolesAllowed("Admin")
    @Operation(summary = "Create a product", description = "Stores a new product inside the in-memory storage. User-provided ID is ignored, auto-incremental ID is always used.")
    public Product create(Product product) { 
        // Repository now enforces auto-incremental IDs - user IDs are ignored
        return productService.createProduct(product); 
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "Find product by ID", description = "Retrieves a single product details from in-memory storage")
    public Product get(@PathParam("id") Long id) throws ProductNotFoundException {
        return productService.getProduct(id); 
    }

    @GET
    @RolesAllowed({"User", "Admin"})
    @Operation(summary = "List all products", description = "Returns a comprehensive list of items available in the in-memory storage")
    public List<Product> getAll() { 
        return productService.getAllProducts(); 
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("Admin")
    @Operation(summary = "Update a product", description = "Updates an existing product in the in-memory storage")
    public Product update(@PathParam("id") Long id, Product product) throws ProductNotFoundException {
        return productService.updateProduct(id, product);
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete a product", description = "Permanently drops a product record by its ID from in-memory storage")
    public void delete(@PathParam("id") Long id) { 
        productService.removeProduct(id); 
    }
}
