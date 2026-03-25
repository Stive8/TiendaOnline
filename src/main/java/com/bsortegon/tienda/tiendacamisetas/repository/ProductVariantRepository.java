package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.id = :id")
    Optional<ProductVariant> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT DISTINCT pv FROM ProductVariant pv JOIN pv.attribute attr WHERE KEY(attr) = :attributeName AND VALUE(attr) = :attributeValue")
    List<ProductVariant> findByAttribute(@Param("attributeName") String attributeName, @Param("attributeValue") String attributeValue);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.category = :category")
    List<ProductVariant> findByCategory(@Param("category") String category);

    @Query("SELECT DISTINCT pv FROM ProductVariant pv JOIN pv.attribute attr " +
           "WHERE pv.product.category = :category AND KEY(attr) = :attributeName AND VALUE(attr) = :attributeValue")
    List<ProductVariant> findByCategoryAndAttribute(@Param("category") String category, 
                                                     @Param("attributeName") String attributeName, 
                                                     @Param("attributeValue") String attributeValue);
}
