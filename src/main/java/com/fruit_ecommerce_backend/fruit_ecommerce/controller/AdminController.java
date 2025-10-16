package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.ProductDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderEntity;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.OrderRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.ProductRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // helper mapper
    private ProductDTO toDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setStock(p.getStock());
        if (p.getImage() != null) {
            dto.setImageBase64(Base64.getEncoder().encodeToString(p.getImage()));
        }
        return dto;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> listProducts() {
        List<ProductDTO> list = productRepository.findAll()
                .stream().map(this::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            product.setImage(file.getBytes());
        }
        Product saved = productRepository.save(product);
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") Product updated,
            @RequestPart(value = "image", required = false) MultipartFile file) throws IOException {

        return productRepository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setStock(updated.getStock());
            if (file != null && !file.isEmpty()) {
                try {
                    existing.setImage(file.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            Product saved = productRepository.save(existing);
            return ResponseEntity.ok(toDTO(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Orders
    @GetMapping
    public ResponseEntity<List<OrderEntity>> listOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    // Users
    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
