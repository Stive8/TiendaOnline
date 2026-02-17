package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.dto.cart.AddCartItemRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.CartItemResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.CartResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.UpdateQuantityRequest;

import java.util.List;

public interface CartService {

    Long createCart();
    CartResponse getCart(Long cartId);
    
    // Cart product management
    CartResponse addProduct(Long cartId, AddCartItemRequest request);
    CartResponse removeItem(Long cartId, Long itemId);
    CartResponse updateQuantity(Long cartId, Long itemId, UpdateQuantityRequest request);
    void clearCart(Long cartId);
    
    // Cart queries
    List<CartItemResponse> getItems(Long cartId);
    Double calculateTotal(Long cartId);
    
}
