package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.RequireRole;
import com.bsortegon.tienda.tiendacamisetas.domain.Attribute;
import com.bsortegon.tienda.tiendacamisetas.domain.AttributeValue;
import com.bsortegon.tienda.tiendacamisetas.domain.UserRole;
import com.bsortegon.tienda.tiendacamisetas.repository.AttributeRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.AttributeValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attributes")
public class AttributeController {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    // --- Atributos ---

    @GetMapping
    public ResponseEntity<List<Attribute>> getAll() {
        return ResponseEntity.ok(attributeRepository.findAll());
    }

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<Attribute> createAttribute(@RequestBody Attribute attribute) {
        if (attributeRepository.existsByNameIgnoreCase(attribute.getName())) {
            throw new RuntimeException("El atributo '" + attribute.getName() + "' ya existe");
        }
        return ResponseEntity.ok(attributeRepository.save(attribute));
    }

    @DeleteMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<String> deleteAttribute(@PathVariable Long id) {
        attributeRepository.deleteById(id);
        return ResponseEntity.ok("Atributo eliminado");
    }

    // --- Valores de atributo ---

    @GetMapping("/{attributeId}/values")
    public ResponseEntity<List<AttributeValue>> getValues(@PathVariable Long attributeId) {
        return ResponseEntity.ok(attributeValueRepository.findByAttributeId(attributeId));
    }

    @PostMapping("/{attributeId}/values")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<AttributeValue> addValue(@PathVariable Long attributeId, @RequestBody AttributeValue attributeValue) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new RuntimeException("Atributo no encontrado con id: " + attributeId));

        if (attributeValueRepository.existsByAttributeIdAndValueIgnoreCase(attributeId, attributeValue.getValue())) {
            throw new RuntimeException("El valor '" + attributeValue.getValue() + "' ya existe en este atributo");
        }

        attributeValue.setAttribute(attribute);
        return ResponseEntity.ok(attributeValueRepository.save(attributeValue));
    }

    @DeleteMapping("/{attributeId}/values/{valueId}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<String> deleteValue(@PathVariable Long attributeId, @PathVariable Long valueId) {
        attributeValueRepository.deleteById(valueId);
        return ResponseEntity.ok("Valor eliminado");
    }
}
