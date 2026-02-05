package com.bsortegon.tienda.tiendacamisetas.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record AddProductCatalogRequest(String name, String brand, String category, BigDecimal basicPrice, List<VarianteDTO> variantes) {
}
