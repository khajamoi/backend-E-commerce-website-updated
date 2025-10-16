//// src/main/java/com/fruit_ecommerce_backend/fruit_ecommerce/controller/PaymentController.java
//package com.fruit_ecommerce_backend.fruit_ecommerce.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import com.fruit_ecommerce_backend.fruit_ecommerce.dto.CreatePaymentOrderRequest;
//import com.fruit_ecommerce_backend.fruit_ecommerce.dto.PaymentVerificationRequest;
//import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderEntity;
//import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Payment;
//import com.fruit_ecommerce_backend.fruit_ecommerce.repository.OrderRepository;
//import com.fruit_ecommerce_backend.fruit_ecommerce.service.PaymentService;
//import com.stripe.Stripe;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//
//import lombok.RequiredArgsConstructor;
//
////@RestController
////@RequestMapping("/api/payments")
////public class PaymentController {
////
////    @Value("${stripe.api.key}")
////    private String stripeApiKey;
////
////    @PostMapping("/create-checkout-session")
////    public ResponseEntity<Map<String, Object>> createCheckoutSession(@RequestBody Map<String, Object> data) throws Exception {
////        // set Stripe secret key
////        Stripe.apiKey = stripeApiKey;
////
////        String baseUrl = "http://localhost:5173"; // your React app (Vite dev server)
////
////        // TODO: In real case, get items & total from `data`
////        SessionCreateParams params = SessionCreateParams.builder()
////                .setMode(SessionCreateParams.Mode.PAYMENT)
////                .setSuccessUrl(baseUrl + "/payment-success?session_id={CHECKOUT_SESSION_ID}")
////                .setCancelUrl(baseUrl + "/payment-failed")
////                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
////                .addLineItem(
////                        SessionCreateParams.LineItem.builder()
////                                .setQuantity(1L)
////                                .setPriceData(
////                                        SessionCreateParams.LineItem.PriceData.builder()
////                                                .setCurrency("inr")
////                                                .setUnitAmount(50000L) // = ₹500.00 (amount in paise)
////                                                .setProductData(
////                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
////                                                                .setName("Fruit Basket")
////                                                                .build()
////                                                )
////                                                .build()
////                                )
////                                .build()
////                )
////                .build();
////
////        // create session
////        Session session = Session.create(params);
////
////        // send response back to frontend
////        Map<String, Object> response = new HashMap<>();
////        response.put("id", session.getId());
////        response.put("url", session.getUrl());
////
////        return ResponseEntity.ok(response);
////    }
////}
//
//
//@RestController
//@RequestMapping("/api/payments")
//@RequiredArgsConstructor
//public class PaymentController {
//    private final PaymentService paymentService;
//    private final OrderRepository orderRepository;
//
//    @PostMapping("/create-order")
//    public ResponseEntity<?> createRazorpayOrder(@RequestBody CreatePaymentOrderRequest request) throws Exception {
//        JSONObject razorpayOrder = paymentService.createRazorpayOrder(request.getOrderId());
//        // Return id, amount and currency for client
//        return ResponseEntity.ok(Map.of(
//            "id", razorpayOrder.getString("id"),
//            "amount", razorpayOrder.getLong("amount"),
//            "currency", razorpayOrder.getString("currency")
//        ));
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest request) {
//        Optional<OrderEntity> optionalOrder = orderRepository.findByRazorpayOrderId(request.getRazorpayOrderId());
//        if (optionalOrder.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Order not found for razorpayOrderId"));
//        }
//
//        OrderEntity order = optionalOrder.get();
//        boolean isValid = paymentService.verifyPayment(request);
//        Payment saved = paymentService.savePayment(order, request, isValid);
//
//        return ResponseEntity.ok(Map.of("success", isValid));
//    }
//}
