package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;
import com.bsortegon.tienda.tiendacamisetas.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<String> getAllProducts() {
        return ResponseEntity.ok("Lista de productos disponibles");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody AddProductCatalogRequest request) {
        productService.save(request);
        return ResponseEntity.ok("Producto '" + request.name() + "' creado exitosamente");
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<String> getProductsByCategory(@PathVariable String categoria) {
        return ResponseEntity.ok("Productos de la categoría: " + categoria);
    }
}