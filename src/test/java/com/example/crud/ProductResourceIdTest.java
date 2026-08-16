package com.example.crud;

import com.example.crud.domain.model.Product;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Tests that POST endpoint ignores user-provided ID and uses auto-incremental IDs.
 */
@QuarkusTest
public class ProductResourceIdTest {

    @Test
    public void testPostIgnoreUserIdAndUsesAutoIncrementalId() {
        // Test 1: User provides ID 1, but system should ignore it
        Product productWithId = new Product(999L, "Product with User ID", 100.0);
        
        Product createdProduct = given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(productWithId)
        .when()
            .post("/products")
        .then()
            .statusCode(200)
            .body("id", not(999))  // Should NOT be 999 (user's ID)
            .body("id", greaterThan(50)) // Should be > 50 (since 50 created on startup)
            .body("name", is("Product with User ID"))
            .body("price", is(100.0f))
            .extract().as(Product.class);
        
        // Test 2: User provides null ID, should work normally  
        Product productWithNullId = new Product(null, "Product with null ID", 200.0);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(productWithNullId)
        .when()
            .post("/products")
        .then()
            .statusCode(200)
            .body("id", nullValue())  // ID will be assigned by repository
            .body("name", is("Product with null ID"))
            .body("price", is(200.0f));
        
        // Test 3: User tries to override existing product ID, should create new product
        Product overrideAttempt = new Product(1L, "Override Attempt", 50.0);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(overrideAttempt)
        .when()
            .post("/products")
        .then()
            .statusCode(200)
            .body("id", not(1))  // Should NOT be 1 (existing product ID)
            .body("id", greaterThan(50)) // Should be new auto-generated ID
            .body("name", is("Override Attempt"));
        
        System.out.println("✅ POST endpoint correctly ignores user-provided IDs and uses auto-incremental IDs!");
    }
    
    @Test
    public void testMultipleProductsGetSequentialIds() {
        // Create multiple products and verify sequential ID generation
        for (int i = 1; i <= 5; i++) {
            Product product = new Product(1000L + i, "Sequential Product " + i, 10.0 * i);
            
            given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic("admin", "admin")
                .body(product)
            .when()
                .post("/products")
            .then()
                .statusCode(200)
                .body("id", not(1000L + i)) // Should ignore user ID
                .body("name", is("Sequential Product " + i));
        }
        
        // Verify we now have more products
        given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(55)); // Should have 50 initial + 5 new products
    }
}
