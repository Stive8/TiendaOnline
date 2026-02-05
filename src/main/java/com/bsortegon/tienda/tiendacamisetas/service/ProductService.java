package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface ProductService {
    
    // Basic CRUD
    Product save(AddProductCatalogRequest request);
    ProductResponse findById(Long id);
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    void deleteById(Long id);
    
    // Specific searches
    List<Product> findByCategory(String category);
    List<Product> findByNameContaining(String name);
    List<Product> findByPriceGreaterThan(double price);

    List<Product> findByCategoria(String categoria);

    List<Product> findByNombreContaining(String nombre);

    List<Product> findByPrecioGreaterThan(double precio);

    // Stock management
    boolean hasStock(Long id, Integer quantity);
    void updateStock(Long id, Integer quantity);
    
    // Validations
    boolean existsById(Long id);
}
