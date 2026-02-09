package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductVariantRepository;
import com.bsortegon.tienda.tiendacamisetas.service.VariantService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private ProductVariantRepository variantRepository;

    @Override
    public ProductVariant findById(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));
    }

    @Override
    @Transactional
    public void updateStock(Long variantId, Long newStock) {
        ProductVariant variant = findById(variantId);
        variant.setStock(newStock);
        variantRepository.save(variant);
    }

    @Override
    @Transactional
    public void updatePrice(Long variantId, Double newPrice) {
        ProductVariant variant = findById(variantId);
        variant.setPrice(newPrice);
        variantRepository.save(variant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        variantRepository.deleteById(id);
    }
}
