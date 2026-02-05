package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOrdenRepository extends JpaRepository<OrderItem, Long> {
}
