package com.bsortegon.tienda.tiendacamisetas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer amount;

    @Column
    private Double unitPrice;

    @ManyToOne
    @JoinColumn (name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn (name = "cart_id")
    private Cart cart;


}
