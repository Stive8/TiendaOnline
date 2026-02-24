package com.bsortegon.tienda.tiendacamisetas.dto.orders;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime createdAt,
        String status,
        Double total,
        List<OrderItemResponse> items,
        String shippingAddress
) {}
