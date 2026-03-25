package com.bsortegon.tienda.tiendacamisetas.dto.response;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        Long categoryId,
        String categoryName,
        String description,
        List<VariantResponse> variants
) {
}
