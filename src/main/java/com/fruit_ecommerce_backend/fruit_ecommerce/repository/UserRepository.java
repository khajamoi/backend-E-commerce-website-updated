package com.fruit_ecommerce_backend.fruit_ecommerce.repository;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
