# GraphQL Integration Guide

This project now includes GraphQL support alongside the existing REST API, providing flexible query capabilities with the same business logic and authentication.

## Overview

**Coexisting APIs:**
- **REST**: `/api/auth/token`, `/products` (existing)
- **GraphQL**: `/graphql` (new)

**Features:**
- Same JWT authentication as REST API
- Complex filtering, sorting, and pagination
- Aggregated queries for statistics
- Basic CRUD operations
- Role-based access control integration

## GraphQL Schema

### Queries

#### Basic Product Queries
```graphql
# Get single product by ID
query {
  product(id: 1) {
    id
    name
    price
  }
}

# Get all products
query {
  products {
    id
    name
    price
  }
}
```

#### Advanced Product Query with Filtering, Sorting, and Pagination
```graphql
query GetProductsWithAdvancedFiltering {
  productsWithFilter(
    filter: {
      nameContains: "phone"
      priceMin: 100
      priceMax: 1000
    }
    sort: {
      field: PRICE
      direction: DESC
    }
    pagination: {
      offset: 0
      limit: 10
    }
  ) {
    edges {
      node { id name price }
      cursor
    }
    pageInfo {
      hasNextPage
      hasPreviousPage
    }
    totalCount
  }
}
```

#### Product Statistics
```graphql
query {
  productStats(
    filter: {
      nameContains: "phone"
    }
  ) {
    count
    averagePrice
    minPrice
    maxPrice
  }
}
```

#### Current User Info
```graphql
query {
  me {
    username
    roles
  }
}
```

### Mutations

#### Create Product
```graphql
mutation {
  createProduct(input: {
    name: "Smart Phone X"
    price: 999.99
  }) {
    id
    name
    price
  }
}
```

#### Update Product
```graphql
mutation {
  updateProduct(id: 1, input: {
    name: "Updated Smart Phone"
    price: 899.99
  }) {
    id
    name
    price
  }
}
```

#### Delete Product
```graphql
mutation {
  deleteProduct(id: 1)
}
```

## Authentication

GraphQL uses the same JWT authentication as the REST API:

1. **Get Token** (via REST):
```bash
curl -X POST http://localhost:8080/api/auth/token \
  -H "Authorization: Basic YWRtaW46YWRtaW4=" \
  -H "Content-Type: application/json"
```

2. **Use GraphQL with Authorization**:
```graphql
# Set Authorization header with JWT token
Authorization: Bearer <your-jwt-token>

# Then execute queries/mutations
```

# Available Roles
- **Admin**: Can create, update, and delete products
- **User**: Can read products and user info

## GraphQL Playground/API

When running in development mode, GraphQL provides:

1. **GraphiQL UI**: `http://localhost:8080/q/graphql-ui/`
2. **GraphQL Schema**: `http://localhost:8080/graphql/schema.graphql`

## Input Types Reference

### ProductFilter
- `nameContains`: String - Filter products by name (case-insensitive)
- `priceMin`: Double - Minimum price filter
- `priceMax`: Double - Maximum price filter

### ProductSort
- `field`: ProductSortField (ID, NAME, PRICE) - Sort field
- `direction`: SortDirection (ASC, DESC) - Sort direction

### Pagination
- `offset`: Int - Number of items to skip (default: 0)
- `limit`: Int - Maximum items to return (default: 20, max: 100)

### ProductInput
- `name`: String (required) - Product name
- `price`: Double (required) - Product price

## Error Handling

GraphQL provides structured error responses:
```json
{
  "errors": [
    {
      "message": "Product not found: 999",
      "code": "PRODUCT_NOT_FOUND",
      "extensions": {
        "productId": "999"
      }
    }
  ],
  "data": null
}
```

## Query Examples

### Complex Filtering and Sorting
```graphql
query {
  productsWithFilter(
    filter: {
      nameContains: "phone"
      priceMin: 500
    }
    sort: {
      field: PRICE
      direction: ASC
    }
    pagination: {
      limit: 5
      offset: 0
    }
  ) {
    totalCount
    pageInfo {
      hasNextPage
    }
    edges {
      node {
        id
        name
        price
      }
    }
  }
}
```

### Product Analytics
```graphql
query {
  productStats {
    count
    averagePrice
    minPrice
    maxPrice
  }
  
  productStats(filter: {
    nameContains: "phone"
  }) {
    count
    averagePrice
  }
}
```

## Implementation Details

**Architecture:**
- GraphQLConfig: Shared business logic with REST
- Resource Layer: GraphQL input/output adapters
- Security: Same JWT integration and role-based access
- Error Handling: GraphQL-specific exception mappers

**Benefits:**
- **Flexible Queries**: Request only needed data
- **Reduced Over-fetching**: Efficient data retrieval
- **Strong Schema**: Type-safe API contracts
- **Single Endpoint**: `/graphql` for all operations
- **增强功能**: 高级筛选和聚合查询

## Testing the Integration

1. **Build & Run**:
```bash
mvn clean install
java -jar target/quarkus-app/quarkus-run.jar
```

2. **Access GraphQL UI**:
```
http://localhost:8080/q/graphql-ui/
```

3. **Test Authentication**:
```bash
# Get JWT token first
curl -X POST http://localhost:8080/api/auth/token \
  -H "Authorization: Basic YWRtaW46YWRtaW4="

# Use token in GraphQL requests
curl -X POST http://localhost:8080/graphql \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"query": "{ products { id name price } }"}'
```

The GraphQL integration provides a powerful, flexible alternative to REST while maintaining security and architectural consistency with your existing application.