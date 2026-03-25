package com.bsortegon.tienda.tiendacamisetas.api;

import com.bsortegon.tienda.tiendacamisetas.config.SecurityUtils;
import com.bsortegon.tienda.tiendacamisetas.domain.Address;
import com.bsortegon.tienda.tiendacamisetas.domain.User;
import com.bsortegon.tienda.tiendacamisetas.dto.address.AddressResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.address.CreateAddressRequest;
import com.bsortegon.tienda.tiendacamisetas.repository.AddressRepository;
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
}