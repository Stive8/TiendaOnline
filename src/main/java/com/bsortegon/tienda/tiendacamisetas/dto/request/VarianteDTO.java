package com.bsortegon.tienda.tiendacamisetas.dto.request;

import java.math.BigDecimal;
import java.util.Map;

public record VarianteDTO(int stock, BigDecimal price, Map<String, String> atributos) {
}
