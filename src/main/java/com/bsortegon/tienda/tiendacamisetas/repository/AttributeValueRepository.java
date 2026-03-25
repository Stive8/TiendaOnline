package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    List<AttributeValue> findByAttributeId(Long attributeId);
    boolean existsByAttributeIdAndValueIgnoreCase(Long attributeId, String value);
    Optional<AttributeValue> findByAttributeIdAndValueIgnoreCase(Long attributeId, String value);
}
