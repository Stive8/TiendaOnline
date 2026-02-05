package com.bsortegon.tienda.tiendacamisetas.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @PostMapping
    public ResponseEntity<String> createOrder() {
        return ResponseEntity.ok("Order created successfully with ID: 1");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok("Order details for ID: " + id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<String> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok("Orders for user: " + userId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok("Order " + id + " status updated to: " + status);
    }
}