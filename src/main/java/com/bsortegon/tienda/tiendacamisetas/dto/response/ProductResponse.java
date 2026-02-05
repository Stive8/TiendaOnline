package com.bsortegon.tienda.tiendacamisetas.dto.response;

import java.util.List;

public record ProductResponse(
        Long id,
        String nombre,
        String descripcion,
        Double precio,
        List<VariantResponse> variants
) {
}
