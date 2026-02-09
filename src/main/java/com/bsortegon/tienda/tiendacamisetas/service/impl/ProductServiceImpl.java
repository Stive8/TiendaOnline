package com.bsortegon.tienda.tiendacamisetas.service.impl;

import com.bsortegon.tienda.tiendacamisetas.domain.Product;
import com.bsortegon.tienda.tiendacamisetas.domain.ProductVariant;
import com.bsortegon.tienda.tiendacamisetas.dto.request.AddProductCatalogRequest;
import com.bsortegon.tienda.tiendacamisetas.dto.response.ProductResponse;
import com.bsortegon.tienda.tiendacamisetas.dto.response.VariantResponse;
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

    @Transactional
    public Product save(AddProductCatalogRequest request) {
        // Buscar producto existente por name y categoría
        Product product = productRepository.findByNameAndCategory(request.name(), request.category())
                .orElseGet(() -> {
                    Product newProduct = new Product();
                    newProduct.setName(request.name());
                    newProduct.setCategory(request.category());
                    newProduct.setDescription(request.description());
                    return newProduct;
                });

        // Crear nuevas variantes
        List<ProductVariant> newVariants = request.variants().stream().map(dto -> {
            ProductVariant variant = new ProductVariant();
            variant.setStock((long) dto.stock());
            variant.setPrice(dto.price());
            variant.setAttribute(dto.attributes());
            variant.setProduct(product);
            return variant;
        }).toList();

        product.getVariants().addAll(newVariants);

        return productRepository.save(product);
    }


    @Override
     public ProductResponse findById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        List<VariantResponse> variantResponses = product.getVariants().stream().map(variant -> new VariantResponse(
                variant.getId(),
                variant.getStock(),
                variant.getPrice(),
                variant.getAttribute()
        )).toList();

        return new ProductResponse(product.getId(), product.getName(), product.getCategory(), product.getDescription(), variantResponses);

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

