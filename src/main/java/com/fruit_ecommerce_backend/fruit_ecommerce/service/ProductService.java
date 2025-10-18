package com.fruit_ecommerce_backend.fruit_ecommerce.service;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Product;
import com.fruit_ecommerce_backend.fruit_ecommerce.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> listAll() {
        List<Product> all = productRepository.findAll();
        all.forEach(Product::computeAndSetOfferPrice);
        return all;
    }

    public Optional<Product> findById(Long id) {
        Optional<Product> p = productRepository.findById(id);
        p.ifPresent(Product::computeAndSetOfferPrice);
        return p;
    }

    @Transactional
    public Product save(Product p) {
        p.computeAndSetOfferPrice();
        return productRepository.save(p);
    }

    @Transactional
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchByName(String name) {
        List<Product> list = productRepository.findByNameContainingIgnoreCase(name);
        list.forEach(Product::computeAndSetOfferPrice);
        return list;
    }

    public List<Product> findByCategory(String category) {
        List<Product> list = productRepository.findByCategory(category);
        list.forEach(Product::computeAndSetOfferPrice);
        return list;
    }

    public List<Product> findActiveOffers() {
        List<Product> list = productRepository.findCurrentlyOnOffer(LocalDate.now());
        list.forEach(Product::computeAndSetOfferPrice);
        return list;
    }
}
