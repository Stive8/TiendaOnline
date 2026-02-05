package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(String category);

    List<Product> findByName(String name);

    List<Product> findByNameContaining(String name);

    List<Product> findByPriceGreaterThan(BigDecimal price);
}

