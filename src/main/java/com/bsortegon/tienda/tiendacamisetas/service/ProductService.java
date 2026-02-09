package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {
    
    Product save(AddProductCatalogRequest request);
    ProductResponse findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    
    List<ProductVariant> findByCategory(String category);
    List<ProductVariant> findByAttribute(String attributeName, String attributeValue);
    List<ProductVariant> findByCategoryAndAttribute(String category, String attributeName, String attributeValue);
    
    boolean existsById(Long id);
}
