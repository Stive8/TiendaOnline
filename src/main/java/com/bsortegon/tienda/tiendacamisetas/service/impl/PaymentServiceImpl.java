package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.Payment;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.domain.status.OrderStatus;
import com.bsortegon.tienda.tiendacamisetas.domain.status.PaymentMethod;
import com.bsortegon.tienda.tiendacamisetas.domain.status.PaymentStatus;
import com.bsortegon.tienda.tiendacamisetas.dto.payment.PaymentResponse;
import com.bsortegon.tienda.tiendacamisetas.repository.OrderRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.PaymentRepository;
import com.bsortegon.tienda.tiendacamisetas.service.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Override
    public PaymentResponse processPayment(Long orderId, PaymentMethod paymentMethod, String idempotencyKey, User authenticatedUser) {
        
        // 0. IDEMPOTENCIA: Verificar si ya existe un pago con esta idempotency key
        Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(idempotencyKey);
        if (existingPayment.isPresent()) {
            // Ya se procesó este pago, retornar el resultado existente
            return mapToPaymentResponse(existingPayment.get());
        }
        
        // 1. Validar que la orden existe Y BLOQUEARLA (pessimistic lock)
        Order order = orderRepository.findByIdWithLock(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Validar que la orden pertenece al usuario autenticado
        if (!order.getUser().getId().equals(authenticatedUser.getId())) {
            throw new RuntimeException("Order does not belong to authenticated user");
        }

        // 3. Validar que la orden está en estado CREATED
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Order must be in CREATED status to process payment. Current status: " + order.getStatus());
        }

        // 4. Validar que no existe un pago previo para esta orden
        if (order.getPayment() != null) {
            throw new IllegalStateException("Payment already exists for this order");
        }

        // 5. Crear el pago (simulado)
        Payment payment = new Payment();
        payment.setAmount(order.getTotal());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId(generateTransactionId());
        payment.setIdempotencyKey(idempotencyKey);
        payment.setOrder(order);

        // 6. Guardar el pago
        Payment savedPayment = paymentRepository.save(payment);

        // 7. Actualizar la orden
        order.setPayment(savedPayment);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // 8. Retornar respuesta
        return mapToPaymentResponse(savedPayment);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getTransactionId(),
                payment.getPaymentDate()
        );
    }
}
