package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Cart;
import com.bsortegon.tienda.tiendacamisetas.domain.CartItem;
import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.AddCartItemRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.CartItemResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.CartResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.cart.UpdateQuantityRequest;
import com.bsortegon.tienda.tiendacamisetas.exception.InsufficientStockException;
import com.bsortegon.tienda.tiendacamisetas.repository.CartItemRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.CartRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductVariantRepository;
import com.bsortegon.tienda.tiendacamisetas.service.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductVariantRepository productVariantRepository;


    @Override
    public Long createCart() {
        Cart cart = new Cart();
        return cartRepository.save(cart).getId();
    }

    @Override
    public CartResponse getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return mapToResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse addProduct(Long cartId, AddCartItemRequest request) {
        if (request.quantity() <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        ProductVariant variant = productVariantRepository.findByIdWithLock(request.variantId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        cartItemRepository.findByCartIdAndProductVariantId(cartId, request.variantId())
                .ifPresentOrElse(item -> {
                    int newQuantity = item.getAmount() + request.quantity();

                    if (variant.getStock() < newQuantity) {
                        throw new InsufficientStockException(
                            variant.getProduct().getName(),
                            variant.getStock(),
                            newQuantity
                        );
                    }

                    item.setAmount(newQuantity);
                    cartItemRepository.save(item);
                }, () -> {
                    if (variant.getStock() < request.quantity()) {
                        throw new InsufficientStockException(
                            variant.getProduct().getName(),
                            variant.getStock(),
                            request.quantity()
                        );
                    }

                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProductVariant(variant);
                    newItem.setAmount(request.quantity());
                    newItem.setUnitPrice(variant.getPrice());
                    cartItemRepository.save(newItem);
                });

        return getCart(cartId);
    }

    @Transactional
    @Override
    public CartResponse removeItem(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        if (!item.getCart().getId().equals(cartId)) {
            throw new RuntimeException("Item does not belong to this cart");
        }
        
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
        cartRepository.flush();
        
        return getCart(cartId);
    }

    @Transactional
    @Override
    public CartResponse updateQuantity(Long cartId, Long itemId, UpdateQuantityRequest request) {
        if (request.quantity() <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }
        
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        if (!item.getCart().getId().equals(cartId)) {
            throw new RuntimeException("Item does not belong to this cart");
        }
        
        // PESSIMISTIC LOCK: Bloquear variant para validar stock
        ProductVariant variant = productVariantRepository.findByIdWithLock(item.getProductVariant().getId())
                .orElseThrow(() -> new RuntimeException("Variant not found"));
        
        if (variant.getStock() < request.quantity()) {
            throw new InsufficientStockException(
                variant.getProduct().getName(),
                variant.getStock(),
                request.quantity()
            );
        }
        
        item.setAmount(request.quantity());
        cartItemRepository.save(item);
        
        return getCart(cartId);
    }

    @Transactional
    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteByCartId(cartId);
    }

    @Override
    public List<CartItemResponse> getItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cart.getCartItems().stream()
                .map(this::mapToItemResponse)
                .toList();
    }

    @Override
    public Double calculateTotal(Long cartId) {
        return getItems(cartId).stream()
                .mapToDouble(CartItemResponse::subtotal)
                .sum();
    }

    // Métodos privados de mapeo
    private CartItemResponse mapToItemResponse(CartItem item) {
        ProductVariant variant = item.getProductVariant();
        return new CartItemResponse(
                item.getId(),
                variant.getId(),
                variant.getProduct().getName(),
                variant.getAttribute(),
                item.getAmount(),
                item.getUnitPrice(),
                item.getAmount() * item.getUnitPrice()
        );
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(this::mapToItemResponse)
                .toList();

        Double total = items.stream()
                .mapToDouble(CartItemResponse::subtotal)
                .sum();

        return new CartResponse(cart.getId(), items, total);
    }
}
