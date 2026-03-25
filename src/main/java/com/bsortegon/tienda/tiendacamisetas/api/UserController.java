package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.RequireRole;
import com.bsortegon.tienda.tiendacamisetas.config.SecurityUtils;
import com.bsortegon.tienda.tiendacamisetas.domain.Address;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.domain.UserRole;
import com.bsortegon.tienda.tiendacamisetas.dto.address.AddressResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.address.CreateAddressRequest;
import com.bsortegon.tienda.tiendacamisetas.repository.AddressRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/me/addresses")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponse> createAddress(@RequestBody CreateAddressRequest request) {
        User user = SecurityUtils.getAuthenticatedUser();
        
        Address address = new Address();
        address.setCalle(request.calle());
        address.setBarrio(request.barrio());
        address.setCiudad(request.ciudad());
        address.setDepartamento(request.departamento());
        address.setCodigoPostal(request.codigoPostal());
        address.setUser(user);
        
        Address saved = addressRepository.save(address);
        
        AddressResponse response = new AddressResponse(
                saved.getId(),
                saved.getCalle(),
                saved.getBarrio(),
                saved.getCiudad(),
                saved.getDepartamento(),
                saved.getCodigoPostal()
        );
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{userId}/promote-admin")
    @RequireRole(UserRole.ADMIN)
    public ResponseEntity<String> promoteToAdmin(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setRol(UserRole.ADMIN);
        userRepository.save(user);
        
        return ResponseEntity.ok("Usuario promovido a ADMIN exitosamente");
    }
}