package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.SecurityUtils;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.dto.payment.PaymentResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.payment.ProcessPaymentRequest;
import com.bsortegon.tienda.tiendacamisetas.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody ProcessPaymentRequest request) {
        // Validar que el idempotencyKey no sea nulo o vacío
        if (request.idempotencyKey() == null || request.idempotencyKey().isBlank()) {
            throw new IllegalArgumentException("Idempotency key is required");
        }
        
        User authenticatedUser = SecurityUtils.getAuthenticatedUser();
        PaymentResponse response = paymentService.processPayment(
                request.orderId(),
                request.paymentMethod(),
                request.idempotencyKey(),
                authenticatedUser
        );
        return ResponseEntity.ok(response);
    }
}
