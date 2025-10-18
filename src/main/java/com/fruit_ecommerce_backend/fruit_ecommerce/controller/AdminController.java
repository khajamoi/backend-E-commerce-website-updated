package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.ProductDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.OrderRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.ProductRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.Base64;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);

    private ProductDTO toDTO(Product p) {
        // ensure offerPrice is up-to-date in DTO
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
            dto.setImageBase64(Base64.getEncoder().encodeToString(p.getImage()));
        }

        return dto;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> listProducts() {
        List<ProductDTO> products = productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws IOException {

        ProductDTO dto = mapper.readValue(productJson, ProductDTO.class);
        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice() != null ? dto.getPrice() : 0.0);
        product.setStock(dto.getStock() != null ? dto.getStock() : 0);
        product.setCategory(dto.getCategory());
        product.setOfferPercentage(dto.getOfferPercentage());
        product.setOfferStartDate(dto.getOfferStartDate());
        product.setOfferEndDate(dto.getOfferEndDate());
        product.setFestivalOffer(dto.getFestivalOffer());
        product.setFestivalName(dto.getFestivalName());

        if (file != null && !file.isEmpty()) {
            product.setImage(file.getBytes());
        }

        // Primary: use offerPrice sent by client (preview). Fallback: compute server side.
        if (dto.getOfferPrice() != null) {
            product.setOfferPrice(dto.getOfferPrice());
        } else {
            product.computeAndSetOfferPrice();
        }

        Product saved = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    @PutMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) throws IOException {

        ProductDTO dto = mapper.readValue(productJson, ProductDTO.class);

        return productRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setDescription(dto.getDescription());
            existing.setPrice(dto.getPrice() != null ? dto.getPrice() : existing.getPrice());
            existing.setStock(dto.getStock() != null ? dto.getStock() : existing.getStock());
            existing.setCategory(dto.getCategory());
            existing.setOfferPercentage(dto.getOfferPercentage());
            existing.setOfferStartDate(dto.getOfferStartDate());
            existing.setOfferEndDate(dto.getOfferEndDate());
            existing.setFestivalOffer(dto.getFestivalOffer());
            existing.setFestivalName(dto.getFestivalName());

            try {
                if (file != null && !file.isEmpty()) {
                    existing.setImage(file.getBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to read uploaded image", e);
            }

            // Prefer client-sent preview offerPrice; otherwise compute server-side
            if (dto.getOfferPrice() != null) {
                existing.setOfferPrice(dto.getOfferPrice());
            } else {
                existing.computeAndSetOfferPrice();
            }

            Product saved = productRepository.save(existing);
            return ResponseEntity.ok(toDTO(saved));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<?>> listOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/users")
    public ResponseEntity<List<?>> listUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
