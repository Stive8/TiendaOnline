package com.bsortegon.tienda.tiendacamisetas.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calle", nullable = false)
    private String calle;

    @Column(name = "barrio", nullable = false)
    private String barrio;

    @Column(name = "ciudad", nullable = false)
    private String ciudad;

    @Column(name = "departamento", nullable = false)
    private String departamento;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "reference")
    private String reference;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
