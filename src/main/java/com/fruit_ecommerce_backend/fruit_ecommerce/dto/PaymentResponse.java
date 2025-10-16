package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private String status;
    private String barcodeBase64;
    private Long orderId;
    private String message;
}
