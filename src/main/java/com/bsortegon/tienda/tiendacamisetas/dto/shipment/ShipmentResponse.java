package com.bsortegon.tienda.tiendacamisetas.dto.shipment;

import com.bsortegon.tienda.tiendacamisetas.domain.status.ShipmentStatus;

import java.time.LocalDateTime;

public record ShipmentResponse(
        Long id,
        Long orderId,
        String trackingNumber,
        ShipmentStatus status,
        String carrier,
        String shippingAddress,
        LocalDateTime estimatedDeliveryDate,
        LocalDateTime actualDeliveryDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
