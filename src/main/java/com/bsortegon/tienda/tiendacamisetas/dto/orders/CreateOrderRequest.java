package com.bsortegon.tienda.tiendacamisetas.dto.orders;

public record CreateOrderRequest(
        Long cartId,
        Long addressId,
        String idempotencyKey  // Token único para prevenir doble creación de orden
) {}
