package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.ProductDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private ProductDTO toDTO(Product p) {
        // ensure pricing is current
        p.computeAndSetOfferPrice();

        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        dto.setCategory(p.getCategory());
        dto.setOfferPercentage(p.getOfferPercentage());
        dto.setOfferStartDate(p.getOfferStartDate());
        dto.setOfferEndDate(p.getOfferEndDate());
        dto.setFestivalOffer(p.getFestivalOffer());
        dto.setFestivalName(p.getFestivalName());
        dto.setOfferActive(p.isOfferActive());
        dto.setOfferPrice(p.getOfferPrice());

        if (p.getImage() != null && p.getImage().length > 0) {
            //  Add the prefix so frontend displays image correctly
            dto.setImageBase64("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(p.getImage()));
        }

        return dto;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> list(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean onlyOffers
    ) {
        List<Product> products;

        if (onlyOffers != null && onlyOffers) {
            products = productService.findActiveOffers();
        } else if (category != null && !category.isEmpty()) {
            products = productService.findByCategory(category);
        } else if (search != null && !search.isEmpty()) {
            products = productService.searchByName(search);
        } else {
            products = productService.listAll();
        }

        List<ProductDTO> dtoList = products.stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        return productService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = productService.listAll().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

}