// src/main/java/com/fruit_ecommerce_backend/fruit_ecommerce/dto/PaymentVerificationRequest.java
package com.fruit_ecommerce_backend.fruit_ecommerce.dto;

import lombok.Data;

/**
 * This DTO receives values from the client after Razorpay success:
 *   razorpayPaymentId  => razorpay_payment_id
 *   razorpayOrderId    => razorpay_order_id
 *   razorpaySignature  => razorpay_signature
 */
@Data
public class PaymentVerificationRequest {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
