package com.example.crud.infrastructure.adapter.output;

import com.example.crud.application.port.output.ProductRepository;
import com.example.crud.domain.model.Product;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Initializes the in-memory storage with sample products on application startup.
 * This class ensures we have 50 products available for testing immediately.
 * Uses pure in-memory storage with no database or SQL.
 */
@ApplicationScoped
public class ProductDataInitializer {
    
    @Inject
    ProductRepository productRepository;
    
    /**
     * Called when the application starts up.
     * Initializes 50 default sample products if memory is empty.
     */
    void onStart(@Observes StartupEvent ev) {
        // Check if products already exist in memory
        List<Product> existingProducts = productRepository.findAll();
        
        if (existingProducts.isEmpty()) {
            System.out.println("🚀 Initializing in-memory data with 50 sample products...");
            initializeSampleProducts();
            System.out.println("✅ In-memory storage initialized with 50 sample products!");
        } else {
            System.out.println("📊 Memory already contains " + existingProducts.size() + " products");
        }
    }
    
    /**
     * Creates and saves 50 diverse sample products in pure memory.
     */
    private void initializeSampleProducts() {
        String[] productNames = {
            // Electronics (1-15)
            "Laptop", "Smartphone", "Tablet", "Wireless Mouse", "Keyboard",
            "Monitor", "Headphones", "Webcam", "USB Hub", "Laptop Stand",
            "Desk Lamp", "External SSD", "Bluetooth Speaker", "Wireless Charger", "HDMI Cable",
            
            // Office Supplies (16-25)
            "Office Chair", "Standing Desk", "Desk Organizer", "Whiteboard", "Pen Set",
            "Notebook Set", "Printer Paper", "Stapler", "Paper Clips", "Binder",
            
            // Kitchen & Home (26-35)
            "Coffee Maker", "Electric Kettle", "Toaster", "Blender", "Microwave",
            "Water Bottle", "Lunch Box", "Kitchen Scale", "Cutting Board", "Food Processor",
            
            // Fitness & Outdoors (36-45)
            "Yoga Mat", "Dumbbells Set", "Resistance Bands", "Jump Rope", "Running Shoes",
            "Backpack", "Water Bottle Pro", "Fitness Tracker", "Gym Bag", "Sports Watch",
            
            // Books & Entertainment (46-50)
            "Fiction Book Bundle", "Programming Guide", "Cookbook Collection", "Art Supplies Set", "Board Game"
        };
        
        double[] productPrices = {
            // Electronics (1-15)
            999.99, 699.99, 399.99, 29.99, 79.99,
            299.99, 129.99, 89.99, 39.99, 49.99,
            34.99, 99.99, 39.99, 24.99, 9.99,
            
            // Office Supplies (16-25)
            199.99, 399.99, 24.99, 89.99, 19.99,
            12.99, 25.99, 15.99, 4.99, 8.99,
            
            // Kitchen & Home (26-35)
            59.99, 39.99, 49.99, 79.99, 149.99,
            15.99, 22.99, 29.99, 18.99, 199.99,
            
            // Fitness & Outdoors (36-45)
            29.99, 49.99, 19.99, 12.99, 89.99,
            45.99, 25.99, 99.99, 35.99, 149.99,
            
            // Books & Entertainment (46-50)
            39.99, 49.99, 29.99, 34.99, 24.99
        };
        
        for (int i = 0; i < productNames.length; i++) {
            try {
                // Create product with null ID to let memory repository generate it
                Product product = new Product(null, productNames[i], productPrices[i]);
                productRepository.save(product);
                System.out.println("  ✓ Created (" + String.format("%02d", i + 1) + "): " + 
                    productNames[i] + " - $" + String.format("%.2f", productPrices[i]));
            } catch (Exception e) {
                System.err.println("  ✗ Failed to create: " + productNames[i] + " - Error: " + e.getMessage());
            }
        }
    }
}
