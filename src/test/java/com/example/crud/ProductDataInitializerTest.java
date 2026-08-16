package com.example.crud;

import com.example.crud.infrastructure.adapter.output.ProductDataInitializer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.greaterThan;
import org.junit.jupiter.api.BeforeEach;

/**
 * Tests that product data initialization works correctly.
 */
@QuarkusTest
public class ProductDataInitializerTest {

    @Inject
    ProductDataInitializer initializer;
    
    @Test
    public void testProductsAreInitialized() {
        // Test that we can retrieve products after initialization
        given()
            .contentType("application/json")
        .when()
            .post("/graphql")
        .then()
            .statusCode(200)
            .body("data.allProducts.size()", greaterThan(0));
        
        // Specifically test we have at least 20 products
        given()
            .contentType("application/json")
            .body("{" +
                "\"query\": " +
                "\"{ allProducts { totalCount } }\"" +
                "}")
        .when()
            .post("/graphql")
        .then()
            .statusCode(200)
            .body("data.allProducts.totalCount", is(50));
    }
}
