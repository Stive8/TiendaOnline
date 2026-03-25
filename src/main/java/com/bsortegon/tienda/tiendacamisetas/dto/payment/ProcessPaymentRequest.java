package com.bsortegon.tienda.tiendacamisetas.dto.payment;

import com.bsortegon.tienda.tiendacamisetas.domain.status.PaymentMethod;

public record ProcessPaymentRequest(
        Long orderId,
        PaymentMethod paymentMethod,
        String idempotencyKey  // Token único para prevenir doble pago
) {}
