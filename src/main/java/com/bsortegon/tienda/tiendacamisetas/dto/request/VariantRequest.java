package com.bsortegon.tienda.tiendacamisetas.dto.request;


import java.util.Map;

public record VariantRequest(int stock, double price, Map<String, String> attributes) {
}
