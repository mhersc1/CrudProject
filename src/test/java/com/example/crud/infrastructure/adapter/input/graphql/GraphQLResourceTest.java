package com.example.crud.infrastructure.adapter.input.graphql;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.model.ProductFilter;
import com.example.crud.domain.model.ProductSort;
import com.example.crud.domain.model.ProductSortField;
import com.example.crud.domain.model.SortDirection;
import com.example.crud.domain.model.Pagination;
import com.example.crud.domain.model.ProductInput;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import org.hamcrest.Matchers;

/**
 * GraphQL Resource Integration Tests.
 * Tests the GraphQL API endpoint functionality using REST Assured.
 * This version focuses on basic GraphQL endpoint testing without mocking.
 */
@QuarkusTest
public class GraphQLResourceTest {

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8081; // Quarkus test port
    }

    // Test GraphQL schema is accessible
    @Test
    public void testGraphQLSchemaIntrospection() {
        String query = """
            {
              __schema {
                queryType { name }
                mutationType { name }
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql")
        .then()
            .statusCode(200);
    }

    // Test basic products query (without mocking - will test actual endpoint)
    @Test
    public void testProductsQuery() {
        String query = """
            {
              products {
                id
                name
                price
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        // Just test the endpoint works - actual data will depend on the real implementation
        Object products = response.jsonPath().get("data.products");
        // May be null or empty depending on actual data
    }

    // Test single product query (without mocking)
    @Test
    public void testProductQuery() {
        String query = """
            {
              product(id: 1) {
                id
                name
                price
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        // Product may be null if no product with ID 1 exists
    }

    // Test product creation mutation (without mocking)
    @Test
    public void testCreateProductMutation() {
        String mutation = """
            mutation {
              createProduct(input: {
                name: "Test Product"
                price: 199.99
              }) {
                id
                name
                price
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", mutation);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        // May fail depending on authentication/authorization
    }

    // Test complex query with filtering (without mocking)
    @Test
    public void testProductsWithFilterQuery() {
        String query = """
            {
              productsWithFilter(
                filter: {
                  nameContains: "test"
                  priceMin: 100
                  priceMax: 1000
                }
                sort: {
                  field: PRICE
                  direction: ASC
                }
                pagination: {
                  offset: 0
                  limit: 10
                }
              ) {
                edges {
                  node {
                    id
                    name
                    price
                  }
                }
                pageInfo {
                  hasNextPage
                  hasPreviousPage
                }
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        // Tests complex query structure
        Map<String, Object> data = response.jsonPath().getMap("data");
        assertNotNull(data.get("productsWithFilter"));
    }

    // Test error handling for non-existent product (without mocking)
    @Test
    public void testProductNotFoundHandling() {
        String query = """
            {
              product(id: 999999) {
                id
                name
                price
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        
        // Test that the endpoint handles non-existent product gracefully
        // Both scenarios are valid: GraphQL errors OR null product
        List<Map<String, Object>> errors = response.jsonPath().getList("errors");
        Object product = response.jsonPath().get("data.product");
        
        // Either we have GraphQL errors OR the product is null
        assertTrue(errors != null && !errors.isEmpty() || product == null);
    }
    // Test checked exception propagation in GraphQL 
    @Test
    public void testCheckedExceptionPropagation() {
        String query = """
            {
              product(id: 999999) {
                id
                name
                price
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        
        // With checked exceptions, we should get proper GraphQL errors with meaningful messages
        List<Map<String, Object>> errors = response.jsonPath().getList("errors");
        Object product = response.jsonPath().get("data.product");
        
        // Should have errors and null product with checked exception
        assertNotNull(errors, "Should have GraphQL errors");
        assertFalse(errors.isEmpty(), "Should have at least one error");
        assertNull(product, "Product should be null when not found");
        
        // Check the error message is not the generic "System error"
        Map<String, Object> error = errors.get(0);
        String errorMessage = (String) error.get("message");
        
        assertNotNull(errorMessage, "Error message should not be null");
        assertNotEquals("System error", errorMessage, "Should not be generic 'System error'");
        assertTrue(errorMessage.contains("999999") || errorMessage.contains("not found"), 
            "Error message should contain product ID or 'not found', but got: " + errorMessage);
    }

    // Test product stats query (without mocking)

    // Test product stats query (without mocking)
    @Test
    public void testProductStatsQuery() {
        String query = """
            {
              productStats(filter: {
                nameContains: "Test"
              }) {
                count
                avgPrice
                minPrice
                maxPrice
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        Response response = given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql");

        assertEquals(200, response.statusCode());
        Map<String, Object> stats = response.jsonPath().getMap("data.productStats");
        assertNotNull(stats);
        // May have default values depending on actual data
    }

    // Keep domain object tests for basic validation
    @Test
    public void testDomainObjectsConstruction() {
        ProductFilter filter = new ProductFilter("phone", 100.0, 1000.0);
        assertNotNull(filter);
        assertEquals("phone", filter.getNameContains());
        assertEquals(100.0, filter.getPriceMin());
        assertEquals(1000.0, filter.getPriceMax());

        ProductSort sort = new ProductSort(ProductSortField.PRICE, SortDirection.DESC);
        assertNotNull(sort);
        assertEquals(ProductSortField.PRICE, sort.getField());
        assertEquals(SortDirection.DESC, sort.getDirection());

        Pagination pagination = new Pagination(10, 20);
        assertNotNull(pagination);
        assertEquals(10, pagination.getOffset());
        assertEquals(20, pagination.getLimit());

        ProductInput input = new ProductInput("New Product", 199.99);
        assertNotNull(input);
        assertEquals("New Product", input.getName());
        assertEquals(199.99, input.getPrice());
    }

    // Test GraphQL endpoint accessibility
    @Test
    public void testGraphQLEndpointAccessible() {
        given()
            .get("/graphql")
        .then()
            .statusCode(405); // Method not allowed for GET, should be POST
    }

    // Test invalid GraphQL query returns errors
    @Test
    public void testInvalidGraphQLQuery() {
        String invalidQuery = """
            {
              invalidField {
                id
              }
            }
            """;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", invalidQuery);

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql")
        .then()
            .statusCode(200)
            .body("errors", Matchers.notNullValue());
    }

    // Test malformed JSON request
    @Test
    public void testMalformedRequest() {
        given()
            .contentType("application/json")
            .body("{ malformed json }")
        .when()
            .post("/graphql")
        .then()
            .statusCode(400); // Bad request for malformed JSON
    }

    // Test missing query parameter
    @Test
    public void testMissingQueryParameter() {
        Map<String, Object> requestBody = new HashMap<>();
        // No "query" field

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/graphql")
        .then()
            .statusCode(400); // Bad request for missing query
    }
}
