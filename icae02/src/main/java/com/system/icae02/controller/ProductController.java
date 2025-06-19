package com.system.icae02.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.system.icae02.model.Product;
import com.system.icae02.service.ProductService;

@RestController
@RequestMapping("/pro")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/{id}")
    public ResponseEntity<List<Product>> filterByCat(@PathVariable("id") int id) {
        return new ResponseEntity<>(service.filterProducts(id), HttpStatus.OK);
    }

    @GetMapping("/district/{district}")
    public ResponseEntity<List<Product>> searchByDistrict(@PathVariable("district") String district) {
        List<Product> products = service.searchByDistrict(district);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Map<String, Object> orderData) {
        // Validate required fields
        String customerId = (String) orderData.get("customerId");
        String date = (String) orderData.get("date");
        @SuppressWarnings("unchecked")
		List<Map<String, Object>> itemsList = (List<Map<String, Object>>) orderData.get("items");

        // Check required fields
        if (customerId == null || date == null || itemsList == null || itemsList.isEmpty()) {
            return buildErrorResponse(400, "BAD REQUEST", "Missing required data", HttpStatus.BAD_REQUEST);
        }

        // Process each order item
        for (Map<String, Object> itemData : itemsList) {
            Integer productId = (Integer) itemData.get("productId");
            Integer qty = (Integer) itemData.get("qty");

            // Validate item data
            if (productId == null || qty == null || qty <= 0) {
                return buildErrorResponse(400, "BAD REQUEST", "Invalid item data", HttpStatus.BAD_REQUEST);
            }

            // Check product exists
            Optional<Product> productOpt = service.findProductById(productId);
            if (!productOpt.isPresent()) {
                return buildErrorResponse(404, "NOT FOUND", "Product not found", HttpStatus.NOT_FOUND);
            }

            Product product = productOpt.get();
            
            // Validate stock quantity
            if (qty > product.getStock()) {
                String message = "Do not have enough stock! Available stock for " + 
                               product.getName() + " is " + product.getStock();
                return buildErrorResponse(406, "NOT Acceptable", message, HttpStatus.NOT_ACCEPTABLE);
            }
        }

        // If all validations pass
        return new ResponseEntity<>("Your order placed", HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
        int code, String status, String message, HttpStatus httpStatus) {
        
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", code);
        errorResponse.put("status", status);
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}