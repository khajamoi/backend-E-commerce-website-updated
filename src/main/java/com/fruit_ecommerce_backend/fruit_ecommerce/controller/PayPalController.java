package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.service.PayPalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
@RequiredArgsConstructor
public class PayPalController {

    private final PayPalService payPalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> req) {
        try {
            double total = Double.parseDouble(req.get("total").toString());
            String cancelUrl = "http://localhost:5173/payment-failed";
            String successUrl = "http://localhost:5173/payment-success";

            Payment payment = payPalService.createPayment(
                    total,
                    "USD",
                    "paypal",
                    "sale",
                    "Fruit Basket Purchase",
                    cancelUrl,
                    successUrl
            );

            // Get approval link
            String approvalLink = payment.getLinks().stream()
                    .filter(link -> link.getRel().equals("approval_url"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No approval_url found"))
                    .getHref();

            return ResponseEntity.ok(Map.of("id", payment.getId(), "url", approvalLink));

        } catch (PayPalRESTException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/execute")
    public ResponseEntity<?> executePayment(@RequestParam String paymentId, @RequestParam String PayerID) {
        try {
            Payment payment = payPalService.executePayment(paymentId, PayerID);
            return ResponseEntity.ok(Map.of("status", payment.getState()));
        } catch (PayPalRESTException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
