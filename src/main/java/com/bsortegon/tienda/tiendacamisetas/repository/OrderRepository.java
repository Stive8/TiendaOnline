package com.bsortegon.tienda.tiendacamisetas.repository;

import com.bsortegon.tienda.tiendacamisetas.domain.Order;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Queries con JOIN FETCH para evitar N+1
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.items " +
           "LEFT JOIN FETCH o.address " +
           "LEFT JOIN FETCH o.payment " +
           "LEFT JOIN FETCH o.shipment " +
           "WHERE o.user = :user")
    List<Order> findByUserWithDetails(@Param("user") User user);
    
    @Query("SELECT o FROM Order o WHERE o.user = :user")
    Page<Order> findByUser(@Param("user") User user, Pageable pageable);
    
    @Query("SELECT DISTINCT o FROM Order o " +
           "LEFT JOIN FETCH o.items " +
           "LEFT JOIN FETCH o.address " +
           "LEFT JOIN FETCH o.payment " +
           "LEFT JOIN FETCH o.shipment " +
           "WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithLock(@Param("id") Long id);
    
    Optional<Order> findByIdempotencyKey(String idempotencyKey);
}
