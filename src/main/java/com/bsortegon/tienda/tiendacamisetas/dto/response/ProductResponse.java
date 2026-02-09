package com.bsortegon.tienda.tiendacamisetas.dto.response;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String category,
        String description,
        List<VariantResponse> variants
) {
}
