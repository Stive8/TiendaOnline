package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.domain.Category;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.response.VariantResponse;
import com.bsortegon.tienda.tiendacamisetas.repository.CategoryRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductRepository;
import com.bsortegon.tienda.tiendacamisetas.repository.ProductVariantRepository;
import com.bsortegon.tienda.tiendacamisetas.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public Product save(AddProductCatalogRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoryId()));

        Product product = productRepository.findByNameAndCategory(request.name(), category)
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setName(request.name());
                    newProduct.setCategory(category);
                    newProduct.setDescription(request.description());
                    return newProduct;
                });

        List<ProductVariant> newVariants = request.variants().stream().map(dto -> {
            ProductVariant variant = new ProductVariant();
            variant.setStock((long) dto.stock());
            variant.setPrice(dto.price());
            variant.setImageUrl(dto.imageUrl());
            variant.setAttribute(dto.attributes());
            variant.setProduct(product);
            return variant;
        }).toList();

        product.getVariants().addAll(newVariants);
        return productRepository.save(product);
    }

    @Transactional
    public ProductResponse update(Long id, AddProductCatalogRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + request.categoryId()));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setCategory(category);

        product.getVariants().clear();
        List<ProductVariant> newVariants = request.variants().stream().map(dto -> {
            ProductVariant variant = new ProductVariant();
            variant.setStock((long) dto.stock());
            variant.setPrice(dto.price());
            variant.setImageUrl(dto.imageUrl());
            variant.setAttribute(dto.attributes());
            variant.setProduct(product);
            return variant;
        }).toList();
        product.getVariants().addAll(newVariants);

        return findById(productRepository.save(product).getId());
    }


    @Override
     public ProductResponse findById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        List<VariantResponse> variantResponses = product.getVariants().stream().map(variant -> new VariantResponse(
                variant.getId(),
                variant.getStock(),
                variant.getPrice(),
                variant.getImageUrl(),
                variant.getAttribute()
        )).toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getDescription(),
                variantResponses);

    }

    @Override
    public List<ProductVariant> findByCategory(String category) {
        return variantRepository.findByCategory(category);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductVariant> findByAttribute(String attributeName, String attributeValue) {
        return variantRepository.findByAttribute(attributeName, attributeValue);
    }

    @Override
    public List<ProductVariant> findByCategoryAndAttribute(String category, String attributeName, String attributeValue) {
        return variantRepository.findByCategoryAndAttribute(category, attributeName, attributeValue);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}

