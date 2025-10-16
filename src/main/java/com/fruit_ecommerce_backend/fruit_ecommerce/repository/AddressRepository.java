package com.fruit_ecommerce_backend.fruit_ecommerce.repository;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Address;
import com.fruit_ecommerce_backend.fruit_ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // Fetch all addresses for a specific user
    List<Address> findByUser(User user);

    // Fetch default address of a specific user
    Address findByUserAndIsDefaultTrue(User user);
}
