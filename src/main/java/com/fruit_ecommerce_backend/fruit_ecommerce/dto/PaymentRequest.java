package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private List<PaymentItem> items;
    private double total;
    private PaymentInfo payment;
    private String paymentMethod; // ONLINE_CARD, UPI, COD
    private Long userId;
}
