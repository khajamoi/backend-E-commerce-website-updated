package com.fruit_ecommerce_backend.fruit_ecommerce.repository;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByRazorpayOrderId(String razorpayOrderId);

    @Query("SELECT DISTINCT o FROM OrderEntity o " +
            "LEFT JOIN FETCH o.items i " +
            "LEFT JOIN FETCH i.product p " +
            "LEFT JOIN FETCH o.user u")
     List<OrderEntity> findAllWithDetails();
}
