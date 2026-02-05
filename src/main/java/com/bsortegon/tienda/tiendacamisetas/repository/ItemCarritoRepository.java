package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCarritoRepository extends JpaRepository<CartItem, Long> {
}
