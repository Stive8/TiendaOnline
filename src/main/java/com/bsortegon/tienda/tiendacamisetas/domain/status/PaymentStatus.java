package com.bsortegon.tienda.tiendacamisetas.domain.status;

public enum PaymentStatus {
    PENDING,    // Payment initiated, waiting for confirmation
    COMPLETED,  // Payment successful
    FAILED,     // Payment failed
    REFUNDED    // Payment refunded
}
