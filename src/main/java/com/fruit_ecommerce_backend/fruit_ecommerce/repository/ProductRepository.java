package com.fruit_ecommerce_backend.fruit_ecommerce.repository;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.offerPercentage IS NOT NULL AND p.offerStartDate <= :today AND p.offerEndDate >= :today")
    List<Product> findCurrentlyOnOffer(@Param("today") LocalDate today);
}
