package com.bsortegon.tienda.tiendacamisetas.dto.cart;

import java.util.Map;

public record CartItemResponse(
        Long id,
        Long variantId,
        String productName,
        Map<String, String> attributes,
        Integer quantity,
        Double unitPrice,
        Double subtotal
) {
}
