package com.bsortegon.tienda.tiendacamisetas.domain;

import com.bsortegon.tienda.tiendacamisetas.domain.status.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @Column(name = "total", nullable = false)
    private double total;

    // Many orders belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One order has many order items
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items;

    // One order is shipped to one address
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    // Una orden tiene un pago (opcional hasta que se pague)
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // Una orden tiene un envío (opcional hasta que se envíe)
    @OneToOne(mappedBy = "order")
    private Shipment shipment;

}
