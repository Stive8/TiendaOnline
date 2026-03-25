package com.bsortegon.tienda.tiendacamisetas.dto.orders;

import com.bsortegon.tienda.tiendacamisetas.domain.status.OrderStatus;

public record UpdateOrderStatusRequest(
        OrderStatus status
) {}
