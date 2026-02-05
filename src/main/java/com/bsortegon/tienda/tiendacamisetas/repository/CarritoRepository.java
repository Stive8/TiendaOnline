package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Cart, Long> {
}
