package com.bsortegon.tienda.tiendacamisetas.dto.shipment;

import java.time.LocalDateTime;

public record CreateShipmentRequest(
        Long orderId,
        String carrier,
        LocalDateTime estimatedDeliveryDate
) {}
