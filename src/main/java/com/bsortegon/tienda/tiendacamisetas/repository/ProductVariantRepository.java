package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

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
