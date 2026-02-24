package com.bsortegon.tienda.tiendacamisetas.dto.orders;

public record OrderItemResponse(
        Long id,
        String productName,
        String variantAttribute,
        Integer quantity,
        Double unitPrice,
        Double subtotal
) {}
