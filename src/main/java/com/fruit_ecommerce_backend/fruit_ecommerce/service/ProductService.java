package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.ProductRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ProductService {
		private final ProductRepository productRepository;
		
		
		public List<Product> listAll(){
		return productRepository.findAll();
		}
		
		
		public Optional<Product> findById(Long id){
		return productRepository.findById(id);
		}
		
		
		public Product save(Product p){
		return productRepository.save(p);
		}
		
		
		public void delete(Long id){ productRepository.deleteById(id); }
}