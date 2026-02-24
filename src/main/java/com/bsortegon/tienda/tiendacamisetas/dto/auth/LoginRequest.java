package com.bsortegon.tienda.tiendacamisetas.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}
