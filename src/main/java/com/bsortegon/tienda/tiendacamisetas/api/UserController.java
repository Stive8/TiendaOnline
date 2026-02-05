package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.dto.request.CreateUserRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.CreateUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody CreateUserRequest request) {
        // Simple response for now
        CreateUserResponse response = new CreateUserResponse(1L, request.email(), "User created successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getUser(@PathVariable Long id) {
        return ResponseEntity.ok("User with ID: " + id);
    }

    @GetMapping
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok("List of all users");
    }
}