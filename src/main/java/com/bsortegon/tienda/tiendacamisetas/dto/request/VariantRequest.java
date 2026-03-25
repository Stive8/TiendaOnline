package com.bsortegon.tienda.tiendacamisetas.dto.request;


import java.util.Map;

public record VariantRequest(int stock, double price, String imageUrl, Map<String, String> attributes) {
}
