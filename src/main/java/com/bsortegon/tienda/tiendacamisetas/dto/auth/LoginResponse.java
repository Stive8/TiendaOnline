package com.bsortegon.tienda.tiendacamisetas.dto.auth;

public record LoginResponse(
        String token,
        String type,
        Long userId,
        String username,
        String email,
        String role
) {
    public LoginResponse(String token, Long userId, String username, String email, String role) {
        this(token, "Bearer", userId, username, email, role);
    }
}

