package com.bsortegon.tienda.tiendacamisetas.dto.request;


import java.util.Map;

public record VariantRequest(int stock, Map<String, String> attributes) {
}
