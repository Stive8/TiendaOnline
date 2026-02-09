package com.bsortegon.tienda.tiendacamisetas.dto.request;

import java.util.List;

public record AddProductCatalogRequest(String name, String category, String description, List<VariantRequest> variants) {
}
