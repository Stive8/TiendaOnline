package com.bsortegon.tienda.tiendacamisetas.dto.orders;

public record CreateOrderRequest(
        Long cartId,
        Long addressId
) {}
