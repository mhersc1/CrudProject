package com.example.crud;

import com.example.crud.domain.model.Product;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Tests the new PUT endpoint for product updates.
 * Verifies that POST creates and PUT updates products correctly.
 */
@QuarkusTest
public class ProductResourceUpdateTest {

    @Test
    public void testUpdateProductWithPut() {
        // First create a product
        Product newProduct = new Product(null, "Test Product", 99.99);
        
        Product createdProduct = given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(newProduct)
        .when()
            .post("/products")
        .then()
            .statusCode(200)
            .body("name", is("Test Product"))
            .body("price", is(99.99f))
            .extract().as(Product.class);
        
        // Now update the product using PUT
        Product updatedProduct = new Product(null, "Updated Test Product", 199.99);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(updatedProduct)
        .when()
            .put("/products/" + createdProduct.id())
        .then()
            .statusCode(200)
            .body("id", is(createdProduct.id().intValue()))
            .body("name", is("Updated Test Product"))
            .body("price", is(199.99f));
        
        // Verify the update persisted by fetching the product
        given()
            .auth().preemptive().basic("user", "user")
        .when()
            .get("/products/" + createdProduct.id())
        .then()
            .statusCode(200)
            .body("name", is("Updated Test Product"))
            .body("price", is(199.99f));
    }
    
    @Test
    public void testPutRequiresAdminRole() {
        // Test that PUT requires Admin role
        Product updateProduct = new Product(null, "Some Product", 50.0);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("user", "user") // User role, not Admin
            .body(updateProduct)
        .when()
            .put("/products/1")
        .then()
            .statusCode(403); // Forbidden
    }
    
    @Test
    public void testPutNonExistentProduct() {
        // Test updating a product that doesn't exist
        Product updateProduct = new Product(null, "Ghost Product", 1.0);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(updateProduct)
        .when()
            .put("/products/99999") // Non-existent ID
        .then()
            .statusCode(404); // Not Found
    }
}
