package com.bsortegon.tienda.tiendacamisetas.domain.status;

public enum ShipmentStatus {
    PENDING,        // Shipment created, waiting to be dispatched
    DISPATCHED,     // Package picked up by carrier
    IN_TRANSIT,     // Package in transit
    OUT_FOR_DELIVERY, // Package out for delivery
    DELIVERED,      // Package delivered successfully
    FAILED,         // Delivery failed
    RETURNED        // Package returned to sender
}
