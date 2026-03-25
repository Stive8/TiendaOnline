package com.bsortegon.tienda.tiendacamisetas.dto.shipment;

import com.bsortegon.tienda.tiendacamisetas.domain.status.ShipmentStatus;

public record UpdateShipmentStatusRequest(
        ShipmentStatus status
) {}
