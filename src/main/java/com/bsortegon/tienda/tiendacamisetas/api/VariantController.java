package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.service.VariantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/variantes")
public class VariantController {

    private final VariantService variantService;

    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getVariant(@PathVariable Long id) {
        return ResponseEntity.ok(variantService.findById(id));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<String> updateStock(
            @PathVariable Long id,
            @RequestParam Long stock) {
        variantService.updateStock(id, stock);
        return ResponseEntity.ok("Stock actualizado exitosamente");
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<String> updatePrice(
            @PathVariable Long id,
            @RequestParam Double price) {
        variantService.updatePrice(id, price);
        return ResponseEntity.ok("Precio actualizado exitosamente");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVariant(@PathVariable Long id) {
        variantService.deleteById(id);
        return ResponseEntity.ok("Variante eliminada exitosamente");
    }
}
