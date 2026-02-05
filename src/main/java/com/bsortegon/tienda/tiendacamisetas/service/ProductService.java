package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    // Basic CRUD
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Page<Product> findAll(Pageable pageable);
    void deleteById(Long id);
    
    // Specific searches
    List<Product> findByCategory(String category);
    List<Product> findByNameContaining(String name);
    List<Product> findByPriceGreaterThan(BigDecimal price);

    List<Product> findByCategoria(String categoria);

    List<Product> findByNombreContaining(String nombre);

    List<Product> findByPrecioGreaterThan(BigDecimal precio);

    // Stock management
    boolean hasStock(Long id, Integer quantity);
    void updateStock(Long id, Integer quantity);
    
    // Validations
    boolean existsById(Long id);
}
