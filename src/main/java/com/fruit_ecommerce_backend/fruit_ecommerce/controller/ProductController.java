package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.ProductDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.ProductService;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // Get all products or search by name
    @GetMapping
    public ResponseEntity<List<ProductDTO>> list(@RequestParam(required = false) String search) {
        List<Product> products;
        if (search != null && !search.isEmpty()) {
            products = productService.searchByName(search);
        } else {
            products = productService.listAll();
        }

        List<ProductDTO> dtoList = products.stream().map(p -> {
            ProductDTO dto = new ProductDTO();
            dto.setId(p.getId());
            dto.setName(p.getName());
            dto.setDescription(p.getDescription());
            dto.setPrice(p.getPrice());
            dto.setStock(p.getStock());

            if (p.getImage() != null) {
                String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(p.getImage());
                dto.setImageBase64(base64Image);
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

    // Get single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> get(@PathVariable Long id) {
        return productService.findById(id)
                .map(p -> {
                    ProductDTO dto = new ProductDTO();
                    dto.setId(p.getId());
                    dto.setName(p.getName());
                    dto.setDescription(p.getDescription());
                    dto.setPrice(p.getPrice());
                    dto.setStock(p.getStock());

                    if (p.getImage() != null) {
                        String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(p.getImage());
                        dto.setImageBase64(base64Image);
                    }

                    return ResponseEntity.ok(dto);
                }).orElse(ResponseEntity.notFound().build());
    }
}
