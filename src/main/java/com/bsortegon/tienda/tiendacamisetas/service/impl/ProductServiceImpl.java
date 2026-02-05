package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.request.VarianteDTO;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductRepository;
import com.bsortegon.tienda.tiendacamisetas.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product save(AddProductCatalogRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setCategory(request.category());
        product.setPrice(request.basicPrice());
        product.setDescription("Product: " + request.name());

        Stream<VarianteDTO> variantEntities = request.variantes().stream();
        return productRepository.save(product);
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Product> findByCategory(String category) {
        return List.of();
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return List.of();
    }

    @Override
    public List<Product> findByPriceGreaterThan(BigDecimal price) {
        return List.of();
    }

    @Override
    public List<Product> findByCategoria(String categoria) {
        return List.of();
    }

    @Override
    public List<Product> findByNombreContaining(String nombre) {
        return List.of();
    }

    @Override
    public List<Product> findByPrecioGreaterThan(BigDecimal precio) {
        return List.of();
    }

    @Override
    public boolean hasStock(Long id, Integer quantity) {
        return false;
    }

    @Override
    public void updateStock(Long id, Integer quantity) {

    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}

