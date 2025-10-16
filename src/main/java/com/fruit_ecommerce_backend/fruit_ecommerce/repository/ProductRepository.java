package com.fruit_ecommerce_backend.fruit_ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByNameContainingIgnoreCase(String name);
	
}