package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    // Gestión de usuarios
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    void deleteById(Long id);
    
    // Validaciones
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsById(Long id);
    
    // Roles
    void makeAdmin(Long userId);
    boolean isAdmin(Long userId);
}
