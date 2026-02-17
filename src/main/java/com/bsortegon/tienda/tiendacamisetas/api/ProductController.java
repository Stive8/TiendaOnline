package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;
import com.bsortegon.tienda.tiendacamisetas.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {


    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductVariant>> getProductsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(productService.findByCategory(category));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductVariant>> searchByAttribute(
            @RequestParam String attributeName,
            @RequestParam String attributeValue) {
        return ResponseEntity.ok(productService.findByAttribute(attributeName, attributeValue));
    }

    @GetMapping("/search/advanced")
    public ResponseEntity<List<ProductVariant>> searchByCategoryAndAttribute(
            @RequestParam String category,
            @RequestParam String attributeName,
            @RequestParam String attributeValue) {
        return ResponseEntity.ok(productService.findByCategoryAndAttribute(category, attributeName, attributeValue));
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody AddProductCatalogRequest request) {
        productService.save(request);
        return ResponseEntity.ok("Producto '" + request.name() + "' creado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.ok("Producto eliminado exitosamente");
    }

}
