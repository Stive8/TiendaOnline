package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.CreateOrderRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.orders.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    
    // Order creation
    OrderResponse createOrderFromCart(CreateOrderRequest request, User authenticatedUser);
    Order save(Order order);
    
    // Queries
    Optional<Order> findById(Long id);
    OrderResponse getOrderById(Long id);
    List<Order> findByUser(User user);
    Page<Order> findByUser(User user, Pageable pageable);
    List<Order> findAll();
    
    // Status management
    OrderResponse updateStatus(Long orderId, String status);
    
    // Validations
    boolean existsById(Long id);
}
