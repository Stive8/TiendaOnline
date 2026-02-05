package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.response.VariantResponse;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductRepository;
import com.bsortegon.tienda.tiendacamisetas.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product save(AddProductCatalogRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setCategory(request.category());
        product.setPrice(request.price());
        List<ProductVariant> variants = request.variants().stream().map(dto -> {
            ProductVariant variant = new ProductVariant();
            variant.setStock((long) dto.stock());
            variant.setAttribute(dto.attributes());
            variant.setProduct(product);
            return variant;
        }).toList();

        product.setVariants(variants);

        return productRepository.save(product);
    }


    @Override
     public ProductResponse findById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        List<VariantResponse> variantResponses = product.getVariants().stream().map(variant -> new VariantResponse(
                variant.getId(),
                variant.getStock(),
                variant.getAttribute()
        )).toList();

        return new ProductResponse(product.getId(), product.getName(), product.getCategory(), product.getPrice(), variantResponses);

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
    public List<Product> findByPriceGreaterThan(double price) {
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
    public List<Product> findByPrecioGreaterThan(double precio) {
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

