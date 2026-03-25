package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.Shipment;
import com.bsortegon.tienda.tiendacamisetas.domain.status.OrderStatus;
import com.bsortegon.tienda.tiendacamisetas.domain.status.ShipmentStatus;
import com.bsortegon.tienda.tiendacamisetas.dto.shipment.ShipmentResponse;
import com.bsortegon.tienda.tiendacamisetas.repository.OrderRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.ShipmentRepository;
import com.bsortegon.tienda.tiendacamisetas.service.ShipmentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Override
    public ShipmentResponse createShipment(Long orderId, String carrier, LocalDateTime estimatedDeliveryDate) {
        
        // 1. Validar que la orden existe Y BLOQUEARLA
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Validar que la orden está en estado PAID
        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Order must be in PAID status to create shipment. Current status: " + order.getStatus());
        }

        // 3. Validar que no existe un envío previo para esta orden
        if (order.getShipment() != null) {
            throw new IllegalStateException("Shipment already exists for this order");
        }

        // 4. Crear el envío
        Shipment shipment = new Shipment();
        shipment.setOrder(order);
        shipment.setTrackingNumber(generateTrackingNumber());
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setCarrier(carrier);
        shipment.setEstimatedDeliveryDate(estimatedDeliveryDate);
        
        // SNAPSHOT: Guardar dirección de envío
        shipment.setShippingAddress(formatAddress(order));

        // 5. Guardar el envío
        Shipment savedShipment = shipmentRepository.save(shipment);

        // 6. Actualizar estado de la orden a SHIPPED
        order.setStatus(OrderStatus.SHIPPED);
        orderRepository.save(order);

        // 7. Retornar respuesta
        return mapToShipmentResponse(savedShipment);
    }

    @Transactional
    @Override
    public ShipmentResponse updateStatus(Long shipmentId, ShipmentStatus newStatus) {
        
        // 1. Obtener envío con lock
        Shipment shipment = shipmentRepository.findByIdWithLock(shipmentId)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));

        // 2. Validar transición de estado
        validateStatusTransition(shipment.getStatus(), newStatus);

        // 3. Actualizar estado
        shipment.setStatus(newStatus);

        // 4. Si se marca como DELIVERED, guardar fecha real de entrega y actualizar orden
        if (newStatus == ShipmentStatus.DELIVERED) {
            shipment.setActualDeliveryDate(LocalDateTime.now());
            
            Order order = shipment.getOrder();
            order.setStatus(OrderStatus.DELIVERED);
            orderRepository.save(order);
        }

        // 5. Guardar cambios
        Shipment updatedShipment = shipmentRepository.save(shipment);

        // 6. Retornar respuesta
        return mapToShipmentResponse(updatedShipment);
    }

    @Override
    public ShipmentResponse getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shipment not found"));
        return mapToShipmentResponse(shipment);
    }

    @Override
    public Optional<ShipmentResponse> getShipmentByOrderId(Long orderId) {
        return shipmentRepository.findByOrderId(orderId)
                .map(this::mapToShipmentResponse);
    }

    @Override
    public Optional<ShipmentResponse> getShipmentByTrackingNumber(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber)
                .map(this::mapToShipmentResponse);
    }

    // Métodos privados

    private void validateStatusTransition(ShipmentStatus currentStatus, ShipmentStatus newStatus) {
        // PENDING -> DISPATCHED
        if (currentStatus == ShipmentStatus.PENDING) {
            if (newStatus != ShipmentStatus.DISPATCHED && newStatus != ShipmentStatus.FAILED) {
                throw new IllegalStateException("Can only change from PENDING to DISPATCHED or FAILED");
            }
        }
        // DISPATCHED -> IN_TRANSIT
        else if (currentStatus == ShipmentStatus.DISPATCHED) {
            if (newStatus != ShipmentStatus.IN_TRANSIT && newStatus != ShipmentStatus.FAILED) {
                throw new IllegalStateException("Can only change from DISPATCHED to IN_TRANSIT or FAILED");
            }
        }
        // IN_TRANSIT -> OUT_FOR_DELIVERY
        else if (currentStatus == ShipmentStatus.IN_TRANSIT) {
            if (newStatus != ShipmentStatus.OUT_FOR_DELIVERY && newStatus != ShipmentStatus.FAILED) {
                throw new IllegalStateException("Can only change from IN_TRANSIT to OUT_FOR_DELIVERY or FAILED");
            }
        }
        // OUT_FOR_DELIVERY -> DELIVERED, FAILED
        else if (currentStatus == ShipmentStatus.OUT_FOR_DELIVERY) {
            if (newStatus != ShipmentStatus.DELIVERED && newStatus != ShipmentStatus.FAILED) {
                throw new IllegalStateException("Can only change from OUT_FOR_DELIVERY to DELIVERED or FAILED");
            }
        }
        // DELIVERED, FAILED, RETURNED are final states
        else if (currentStatus == ShipmentStatus.DELIVERED || 
                 currentStatus == ShipmentStatus.FAILED || 
                 currentStatus == ShipmentStatus.RETURNED) {
            throw new IllegalStateException("Cannot change status of " + currentStatus + " shipment");
        }
    }

    private String generateTrackingNumber() {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    private String formatAddress(Order order) {
        var address = order.getAddress();
        return String.format("%s, %s, %s, %s",
                address.getCalle(),
                address.getBarrio(),
                address.getCiudad(),
                address.getDepartamento()
        );
    }

    private ShipmentResponse mapToShipmentResponse(Shipment shipment) {
        return new ShipmentResponse(
                shipment.getId(),
                shipment.getOrder().getId(),
                shipment.getTrackingNumber(),
                shipment.getStatus(),
                shipment.getCarrier(),
                shipment.getShippingAddress(),
                shipment.getEstimatedDeliveryDate(),
                shipment.getActualDeliveryDate(),
                shipment.getCreatedAt(),
                shipment.getUpdatedAt()
        );
    }
}
