package com.bsortegon.tienda.tiendacamisetas.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter @Setter @NoArgsConstructor
@Table (name = "variant_product")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock", nullable = false)
    private Long stock;

    @ElementCollection
    @CollectionTable(name = "product_attribute", joinColumns = @JoinColumn (name = "product_id"))
    @MapKeyColumn(name = "attribute_name")
    @Column(name = "attribute_value")
    private Map< String, String> attribute = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
