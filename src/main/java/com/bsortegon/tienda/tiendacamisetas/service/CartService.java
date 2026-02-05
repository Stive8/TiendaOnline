package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.CartItem;


import java.util.List;

public interface CartService {
    
    // Cart product management
    void addProduct(Long productId, Integer quantity);
    void removeProduct(Long productId);
    void updateQuantity(Long productId, Integer quantity);
    void clear();
    
    // Cart queries
    List<CartItem> getItems();
    Integer getTotalItems();
    double getTotal();
    boolean isEmpty();
    
    // Validations
    boolean containsProduct(Long productId);
}
