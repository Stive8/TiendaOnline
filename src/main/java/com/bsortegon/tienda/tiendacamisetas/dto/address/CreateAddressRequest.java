package com.bsortegon.tienda.tiendacamisetas.dto.address;

public record CreateAddressRequest(
        String calle,
        String barrio,
        String ciudad,
        String departamento,
        String codigoPostal
) {}
