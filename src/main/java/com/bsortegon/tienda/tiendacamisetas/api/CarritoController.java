package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCarritoRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @GetMapping
    public ResponseEntity<String> getCart() {
        return ResponseEntity.ok("Contenido del carrito de compras");
    }

    @PostMapping("/agregar")
    public ResponseEntity<String> addToCart(@RequestBody AddProductCarritoRequest request) {
        return ResponseEntity.ok("Producto " + request.id() + " agregado al carrito (cantidad: " + request.cantidad() + ")");
    }

    @DeleteMapping("/producto/{id}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long id) {
        return ResponseEntity.ok("Producto " + id + " eliminado del carrito");
    }

    @DeleteMapping("/limpiar")
    public ResponseEntity<String> clearCart() {
        return ResponseEntity.ok("Carrito limpiado exitosamente");
    }
}