//package com.fruit_ecommerce_backend.fruit_ecommerce.service;
//
//import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderEntity;
//import com.fruit_ecommerce_backend.fruit_ecommerce.entity.UserPayment;
//import com.fruit_ecommerce_backend.fruit_ecommerce.repository.UserPaymentRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserPaymentService {
//
//    private final UserPaymentRepository paymentRepository;
//
//    public UserPayment processPayment(UserPayment payment, OrderEntity order) {
//        // Simulate payment processing logic
//        if (payment.getCardNumber().length() == 16 && payment.getCvv().length() == 3) {
//            payment.setStatus("SUCCESS");
//        } else {
//            payment.setStatus("FAILED");
//        }
//
//        payment.setOrder(order);
//        return paymentRepository.save(payment);
//    }
//}
