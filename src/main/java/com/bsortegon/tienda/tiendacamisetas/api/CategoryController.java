package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.RequireRole;
import com.bsortegon.tienda.tiendacamisetas.domain.Category;
import com.bsortegon.tienda.tiendacamisetas.domain.UserRole;
import com.bsortegon.tienda.tiendacamisetas.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @PostMapping
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<Category> create(@RequestBody Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new RuntimeException("La categoría '" + category.getName() + "' ya existe");
        }
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return ResponseEntity.ok("Categoría eliminada");
    }
}
