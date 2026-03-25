package com.bsortegon.tienda.tiendacamisetas.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Entity
@Getter @Setter @NoArgsConstructor
@Table (name = "variant_product")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "stock", nullable = false)
    private Long stock;

    @Column(name = "price", nullable = false)
    private Double price;

    @ElementCollection
    @CollectionTable(name = "product_attributes", joinColumns = @JoinColumn (name = "variant_id"))
    @MapKeyColumn(name = "attribute_name")
    @Column(name = "attribute_value")
    private Map< String, String> attribute = new HashMap<>();

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

}
