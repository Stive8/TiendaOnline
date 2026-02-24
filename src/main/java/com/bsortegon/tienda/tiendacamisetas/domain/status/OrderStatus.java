package com.bsortegon.tienda.tiendacamisetas.domain.status;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED,
    REFUNDED,
    FAILED
}
