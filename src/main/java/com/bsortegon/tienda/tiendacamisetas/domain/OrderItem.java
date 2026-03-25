package com.bsortegon.tienda.tiendacamisetas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private double unitPrice;

    // SNAPSHOT: Datos del producto en el momento de la compra
    @Column(nullable = false)
    private String productName;

    @ElementCollection
    @CollectionTable(name = "order_item_attributes", joinColumns = @JoinColumn(name = "order_item_id"))
    @MapKeyColumn(name = "attribute_name")
    @Column(name = "attribute_value")
    private Map<String, String> productAttributes;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Mantener referencia para trazabilidad (opcional)
    @ManyToOne
    @JoinColumn(name = "productVariant_id")
    private ProductVariant productVariant;

}
