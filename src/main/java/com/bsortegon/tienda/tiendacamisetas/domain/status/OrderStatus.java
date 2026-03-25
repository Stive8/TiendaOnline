package com.bsortegon.tienda.tiendacamisetas.domain.status;

public enum OrderStatus {
    CREATED,    // Order created, waiting for payment
    PAID,       // Payment confirmed
    SHIPPED,    // Order in transit
    DELIVERED,  // Order completed
    CANCELLED   // Order cancelled
}
