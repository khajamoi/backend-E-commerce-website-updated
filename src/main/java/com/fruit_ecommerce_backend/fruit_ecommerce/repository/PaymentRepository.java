// src/main/java/com/fruit_ecommerce_backend/fruit_ecommerce/repository/PaymentRepository.java
package com.fruit_ecommerce_backend.fruit_ecommerce.repository;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
}
