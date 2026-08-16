package com.example.crud.application.port.input;

import com.example.crud.domain.model.Product;
import com.example.crud.domain.model.ProductFilter;
import com.example.crud.domain.model.ProductSort;
import com.example.crud.domain.model.Pagination;

import java.util.List;

/**
 * Application port for advanced product queries with filtering, sorting, and pagination.
 * This interface defines what the application offers in terms of sophisticated product querying.
 */
public interface ProductQueryService {
    
    /**
     * Retrieves products with filtering, sorting, and pagination applied.
     * 
     * @param filter Filter criteria (name contains, price range)
     * @param sort Sort specification (field and direction)
     * @param pagination Pagination parameters (limit and offset)
     * @return Filtered, sorted, and paginated list of products
     */
    List<Product> filterProducts(ProductFilter filter, ProductSort sort, Pagination pagination);
    
    /**
     * Counts products that match the given filter criteria.
     * 
     * @param filter Filter criteria (name contains, price range)
     * @return Count of products matching the filter
     */
    int countProducts(ProductFilter filter);
}
