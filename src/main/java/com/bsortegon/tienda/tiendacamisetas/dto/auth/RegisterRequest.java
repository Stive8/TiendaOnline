package com.bsortegon.tienda.tiendacamisetas.dto.auth;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String phoneNumbers
) {
}
