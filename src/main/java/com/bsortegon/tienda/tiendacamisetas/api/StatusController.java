package com.bsortegon.tienda.tiendacamisetas.api;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/StatusDB")
    public ResponseEntity<String> statusDB() {
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return ResponseEntity.ok("DB Connection: OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("DB Connection: FAILED - " + e.getMessage());
        }
    }
}
