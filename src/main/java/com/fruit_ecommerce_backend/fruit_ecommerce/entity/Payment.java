// src/main/java/com/fruit_ecommerce_backend/fruit_ecommerce/entity/Payment.java
package com.fruit_ecommerce_backend.fruit_ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private String status; // SUCCESS, FAILED

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}
