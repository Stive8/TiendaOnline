package com.bsortegon.tienda.tiendacamisetas.service;

import com.bsortegon.tienda.tiendacamisetas.domain.User;

import java.util.Optional;

public interface AuthService {
    
    // Autenticación
    boolean authenticate(String username, String password);
    void logout();
    
    // Sesión
    Optional<User> getCurrentUser();
    boolean isAuthenticated();
    
    // Registro
    User register(String username, String email, String password);
    
    // Validaciones de seguridad
    boolean isValidPassword(String password);
    String encodePassword(String password);
}