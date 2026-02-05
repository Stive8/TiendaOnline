package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public ResponseEntity<String> getAllProducts() {
        return ResponseEntity.ok("Lista de productos disponibles");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok("Producto con ID: " + id);
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody AddProductCatalogRequest request) {
        return ResponseEntity.ok("Producto '" + request.name() + "' creado exitosamente");
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<String> getProductsByCategory(@PathVariable String categoria) {
        return ResponseEntity.ok("Productos de la categoría: " + categoria);
    }
}