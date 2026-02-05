package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    
    // Order creation
    Order createOrder(User user, Long addressId);
    Order save(Order order);
    
    // Queries
    Optional<Order> findById(Long id);
    List<Order> findByUser(User user);
    Page<Order> findByUser(User user, Pageable pageable);
    List<Order> findAll();
    
    // Status management
    void updateStatus(Long orderId, String status);
    
    // Validations
    boolean existsById(Long id);
}
