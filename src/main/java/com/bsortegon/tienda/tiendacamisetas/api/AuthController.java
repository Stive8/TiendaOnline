package com.bsortegon.tienda.tiendacamisetas.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok("Login exitoso para: " + email);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout exitoso");
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser() {
        return ResponseEntity.ok("Usuario actual autenticado");
    }
}