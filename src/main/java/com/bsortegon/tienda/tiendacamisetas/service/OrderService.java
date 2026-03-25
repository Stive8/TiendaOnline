package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.domain.status.OrderStatus;
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
    OrderResponse getOrderByIdForUser(Long id, User user);
    List<Order> findByUser(User user);
    Page<OrderResponse> findByUser(User user, Pageable pageable);
    List<OrderResponse> findAll();
    
    // Status management
    OrderResponse updateStatus(Long orderId, OrderStatus status);
    
    // Validations
    boolean existsById(Long id);
}
