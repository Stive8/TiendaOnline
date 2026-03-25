package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.domain.status.PaymentMethod;
import com.bsortegon.tienda.tiendacamisetas.dto.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(Long orderId, PaymentMethod paymentMethod, String idempotencyKey, User authenticatedUser);
}
