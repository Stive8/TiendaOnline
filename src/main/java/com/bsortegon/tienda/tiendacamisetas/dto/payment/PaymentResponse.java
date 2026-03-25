package com.bsortegon.tienda.tiendacamisetas.dto.payment;

import com.bsortegon.tienda.tiendacamisetas.domain.status.PaymentMethod;
import com.bsortegon.tienda.tiendacamisetas.domain.status.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        double amount,
        PaymentMethod paymentMethod,
        PaymentStatus status,
        String transactionId,
        LocalDateTime paymentDate
) {}
