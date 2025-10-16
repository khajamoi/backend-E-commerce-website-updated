package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentItem {
    private Long productId;
    private int qty;
    private Double price;
}
