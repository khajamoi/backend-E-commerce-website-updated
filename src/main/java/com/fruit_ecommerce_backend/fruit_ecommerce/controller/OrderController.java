package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.OrderDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.dto.OrderResponseDTO;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderEntity;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderItem;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.ProductRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserRepository;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderDTO dto, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        List<OrderItem> items = new ArrayList<>();
        for (OrderDTO.OrderItemDTO it : dto.getItems()) {
            Product p = productRepository.findById(it.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + it.getProductId()));

            if (p.getStock() < it.getQty()) {
                return ResponseEntity.badRequest().body("Insufficient stock for " + p.getName());
            }

            OrderItem oi = OrderItem.builder()
                    .product(p)
                    .quantity(it.getQty())
                    .price(p.getPrice())
                    .build();
            items.add(oi);

            // reduce stock
            p.setStock(p.getStock() - it.getQty());
            productRepository.save(p);
        }

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .items(items)
                .total(dto.getTotal())
                .status("PENDING")
                .build();

        // link back from items to order
        items.forEach(i -> i.setOrder(order));

        OrderEntity saved = orderService.placeOrder(order);

        return ResponseEntity.ok(saved.getId());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin());
    }

    // single order (optional)
    @GetMapping("/admin/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
