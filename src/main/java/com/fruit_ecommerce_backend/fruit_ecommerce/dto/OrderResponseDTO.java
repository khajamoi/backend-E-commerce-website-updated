package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private String status;
    private double total;
    private Instant createdAt;

    // User info
    private Long userId;
    private String userName;
    private String userEmail;

    // Payment info
    private String gatewayOrderId;
    private String razorpayOrderId;
    private String razorpayPaymentId;

    private List<OrderItemDTO> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItemDTO {
        private Long id;
        private Long productId;
        private String productName;
        private double price;
        private int quantity;
    }
}
