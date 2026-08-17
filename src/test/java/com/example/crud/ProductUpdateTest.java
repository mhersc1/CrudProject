package com.example.crud;

import com.example.crud.domain.model.Product;
import com.example.crud.infrastructure.adapter.output.InMemoryProductRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * Tests the update functionality to ensure it uses dedicated update methods
 * and doesn't interfere with auto-incremental ID generation.
 */
@QuarkusTest
public class ProductUpdateTest {

    @Test
    public void testUpdatePreservesOriginalId() {
        // First, check existing products to get a valid ID
        Product[] initialProducts = given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .extract().as(Product[].class);
        
        // Use the first product for testing update
        Long originalId = initialProducts[0].id();
        String originalName = initialProducts[0].name();
        double originalPrice = initialProducts[0].price();
        
        // Update the product using PUT
        Product updatedProduct = new Product(null, "UPDATED PRODUCT NAME", 999.99);
        
        Product returnedProduct = given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(updatedProduct)
        .when()
            .put("/products/" + originalId)
        .then()
            .statusCode(200)
            .body("id", equalTo(originalId.intValue()))  // ID should be preserved!
            .body("name", is("UPDATED PRODUCT NAME"))
            .body("price", is(999.99f))
            .extract().as(Product.class);
        
        // Verify no new product was created (total count should remain 50)
        given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", is(50));  // Should still be 50, not 51
        
        System.out.println("✅ Update preserved original ID: " + originalId + " → " + returnedProduct.id());
    }
    
    @Test
    public void testCreateNewProductStillUsesAutoIncrementalId() {
        // Create a completely new product
        Product newProduct = new Product(9999L, "COMPLETLY NEW PRODUCT", 123.45);
        
        Product createdProduct = given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(newProduct)
        .when()
            .post("/products")
        .then()
            .statusCode(200)
            .body("id", not(9999))  // Should NOT use the user-specified ID
            .body("id", equalTo(51))   // Should use auto-incremental ID
            .body("name", is("COMPLETLY NEW PRODUCT"))
            .extract().as(Product.class);
        
        // Now we should have 51 products total
        given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", is(51));  // Should be 51 now (50 initial + 1 new)
        
        System.out.println("✅ New product created with auto-incremental ID: " + createdProduct.id());
    }
    
    @Test
    public void testUpdateAndCreateAreSeparate() {
        // Get current product count
        Product[] productsBefore = given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .extract().as(Product[].class);
        
        int initialCount = productsBefore.length;
        
        // Update an existing product
        Product updateData = new Product(null, "UPDATED NAME", 111.11);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(updateData)
        .when()
            .put("/products/" + productsBefore[0].id())
        .then()
            .statusCode(200);
        
        // Count should be the same (update doesn't create new product)
        given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", is(initialCount));
        
        // Create a new product
        Product createData = new Product(8888L, "CREATED NAME", 222.22);
        
        given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin")
            .body(createData)
        .when()
            .post("/products")
        .then()
            .statusCode(200);
        
        // Count should be incremented by 1 (create adds new product)
        given()
            .auth().preemptive().basic("admin", "admin")
        .when()
            .get("/products")
        .then()
            .statusCode(200)
            .body("size()", is(initialCount + 1));
        
        System.out.println("✅ Update and Create are properly separated!");
    }
}
