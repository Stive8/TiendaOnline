package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Address, Long> {
}
