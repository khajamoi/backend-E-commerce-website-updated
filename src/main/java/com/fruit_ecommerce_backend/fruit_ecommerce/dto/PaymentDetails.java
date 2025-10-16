package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDetails {
    private String cardHolder;
    private String cardNumber;
    private String expiry;
    private String cvv;
}
