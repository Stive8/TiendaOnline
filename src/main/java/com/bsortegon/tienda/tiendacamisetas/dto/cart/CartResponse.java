package com.bsortegon.tienda.tiendacamisetas.dto.cart;

import java.util.List;

public record CartResponse(
        Long id,
        List<CartItemResponse> items,
        Double total
) {
}
