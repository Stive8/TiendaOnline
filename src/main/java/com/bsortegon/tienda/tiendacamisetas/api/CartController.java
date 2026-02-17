package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.dto.cart.AddCartItemRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.CartResponse;
import com.bsortegon.tienda.tiendacamisetas.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/create")
    public ResponseEntity<Long> createCart() {
        return ResponseEntity.ok(cartService.createCart());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long id) {
        return ResponseEntity.ok(cartService.getCart(id));
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long cartId, @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addProduct(cartId, request));
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        return ResponseEntity.ok(cartService.removeItem(cartId, itemId));
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }
}