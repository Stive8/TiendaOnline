package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.RequireRole;
import com.bsortegon.tienda.tiendacamisetas.domain.UserRole;
import com.bsortegon.tienda.tiendacamisetas.service.impl.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private SupabaseStorageService storageService;

    @PostMapping("/upload")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        String url = storageService.uploadImage(file);
        return ResponseEntity.ok(Map.of("imageUrl", url));
    }
}
