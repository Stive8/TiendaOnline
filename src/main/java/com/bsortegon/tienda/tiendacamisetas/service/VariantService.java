package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;

public interface VariantService {
    
    ProductVariant findById(Long id);
    void updateStock(Long variantId, Long newStock);
    void updatePrice(Long variantId, Double newPrice);
    void deleteById(Long id);
}
