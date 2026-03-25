package com.bsortegon.tienda.tiendacamisetas.dto.response;

import java.util.Map;

public record VariantResponse(
        Long id,
        Long stock,
        Double price,
        String imageUrl,
        Map<String, String> attributes
) {
}
