package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.status.ShipmentStatus;
import com.bsortegon.tienda.tiendacamisetas.dto.shipment.ShipmentResponse;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShipmentService {
    
    ShipmentResponse createShipment(Long orderId, String carrier, LocalDateTime estimatedDeliveryDate);
    
    ShipmentResponse updateStatus(Long shipmentId, ShipmentStatus newStatus);
    
    ShipmentResponse getShipmentById(Long id);
    
    Optional<ShipmentResponse> getShipmentByOrderId(Long orderId);
    
    Optional<ShipmentResponse> getShipmentByTrackingNumber(String trackingNumber);
}
