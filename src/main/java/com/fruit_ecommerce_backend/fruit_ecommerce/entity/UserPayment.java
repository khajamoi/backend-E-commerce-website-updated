package com.fruit_ecommerce_backend.fruit_ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status; 
    private double amount;

    private String cardHolder;
    private String cardLast4;
    private String expiryMasked;
    private String cardToken;

    @Lob
    private String barcodeBase64;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    private Instant createdAt = Instant.now();
}



//package com.fruit_ecommerce_backend.fruit_ecommerce.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.Instant;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.Instant;
//
//@Entity
//@Table(name = "user_payments")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserPayment {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // Only store safe info
//    private String cardHolder;
//    private String cardLast4;     // last 4 digits only
//    private String expiryMasked;  // e.g. MM/YY
//    private String cardToken;     // hashed/tokenized value
//
//    private double amount;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;            // ✅ link payment with user
//
//    @OneToOne
//    private OrderEntity order;
//
//    private String status;        // SUCCESS, FAILED, COD_PENDING, UPI_PENDING
//
//    @Lob
//    @Column(columnDefinition = "TEXT")
//    private String barcodeBase64; // ✅ store QR (UPI / COD)
//
//    private Instant createdAt = Instant.now();
//}


//package com.fruit_ecommerce_backend.fruit_ecommerce.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.Instant;
//
//@Entity
//@Table(name = "user_payments")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserPayment {
//    
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String cardHolder;
//    private String cardNumber;  // In real-world apps, don't store raw card numbers!
//    private String expiry;
//    private String cvv;
//
//    private double amount;
//
//    @OneToOne
//    private OrderEntity order;
//
//    private String status; // SUCCESS, FAILED
//
//    private Instant createdAt = Instant.now();
//}
