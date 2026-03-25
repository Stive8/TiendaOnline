package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.SecurityUtils;
import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.CreateOrderRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.UpdateOrderStatusRequest;
import com.bsortegon.tienda.tiendacamisetas.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        // Validar que el idempotencyKey no sea nulo o vacío
        if (request.idempotencyKey() == null || request.idempotencyKey().isBlank()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }
        
        User authenticatedUser = SecurityUtils.getAuthenticatedUser();
        OrderResponse response = orderService.createOrderFromCart(request, authenticatedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        User authenticatedUser = SecurityUtils.getAuthenticatedUser();
        OrderResponse response = orderService.getOrderByIdForUser(id, authenticatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(Pageable pageable) {
        User authenticatedUser = SecurityUtils.getAuthenticatedUser();
        Page<OrderResponse> orders = orderService.findByUser(authenticatedUser, pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateStatus(id, request.status());
        return ResponseEntity.ok(response);
    }
}