package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.dto.*;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.*;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.*;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class UserPaymentController {

    private final PaymentService paymentService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest req,
                                            Authentication authentication) {
        try {
            // --- Resolve user from JWT / Authentication ---
            User user = null;
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName(); // email from JWT
                user = userRepository.findByEmail(email).orElse(null);
            }

            // fallback to userId from frontend if not authenticated
            if (user == null && req.getUserId() != null) {
                user = userRepository.findById(req.getUserId()).orElse(null);
            }

            if (user == null) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // --- Map frontend payment method ---
            String paymentMethod = req.getPaymentMethod();
            if ("ONLINE".equalsIgnoreCase(paymentMethod)) paymentMethod = "ONLINE_CARD";

            // --- Create order ---
            OrderEntity order = paymentService.createOrderFromItems(req.getItems(), req.getTotal(), user.getId());
            order.setUser(user);
            orderRepository.save(order);

            // --- Process payment ---
            UserPayment savedPayment;
            switch (paymentMethod.toUpperCase()) {
                case "ONLINE_CARD":
                    if (req.getPayment() == null)
                        return ResponseEntity.badRequest().body("Payment info required for card");
                    savedPayment = paymentService.processCardPayment(order, req.getPayment(), user);
                    if ("SUCCESS".equals(savedPayment.getStatus())) {
                        order.setStatus("PAID");
                        orderRepository.save(order);
                    }
                    break;

                case "COD":
                    savedPayment = paymentService.createCodPayment(order, user);
                    order.setStatus("COD_PENDING");
                    orderRepository.save(order);
                    break;

                case "UPI":
                    savedPayment = paymentService.createUpiPayment(order, user);
                    order.setStatus("UPI_PENDING");
                    orderRepository.save(order);
                    break;

                default:
                    return ResponseEntity.badRequest().body("Invalid payment method");
            }

            PaymentResponse resp = new PaymentResponse(
                    savedPayment.getId(),
                    savedPayment.getStatus(),
                    savedPayment.getBarcodeBase64(),
                    order.getId(),
                    "Payment processed"
            );

            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Payment failed: " + e.getMessage());
        }
    }
}
