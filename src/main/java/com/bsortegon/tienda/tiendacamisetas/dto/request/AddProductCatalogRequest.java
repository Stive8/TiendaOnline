package com.bsortegon.tienda.tiendacamisetas.dto.request;

import java.util.List;

public record AddProductCatalogRequest(String name, Long categoryId, String description, List<VariantRequest> variants) {
}
