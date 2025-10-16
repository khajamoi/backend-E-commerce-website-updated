package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {
    private String cardHolder;
    private String cardNumber;
    private String expiry;
    private String cvv;
}
