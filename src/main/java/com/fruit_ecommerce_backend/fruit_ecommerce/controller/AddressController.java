package com.fruit_ecommerce_backend.fruit_ecommerce.controller;

import com.fruit_ecommerce_backend.fruit_ecommerce.entity.Address;
import com.fruit_ecommerce_backend.fruit_ecommerce.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * Add a new address for a specific user.
     */
    @PostMapping("/user/{userId}/add")
    public Address addAddress(@PathVariable Long userId, @RequestBody Address address) {
        return addressService.addAddress(userId, address);
    }

    /**
     * Get all addresses of a specific user.
     */
    @GetMapping("/user/{userId}")
    public List<Address> getAddressesByUser(@PathVariable Long userId) {
        return addressService.getAddressesByUser(userId);
    }

    /**
     * Update an existing address.
     */
    @PutMapping("/{addressId}")
    public Address updateAddress(@PathVariable Long addressId, @RequestBody Address address) {
        return addressService.updateAddress(addressId, address);
    }

    /**
     * Delete an address by ID.
     */
    @DeleteMapping("/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return "Address deleted successfully!";
    }

    /**
     * Get default address of a user.
     */
    @GetMapping("/user/{userId}/default")
    public Address getDefaultAddress(@PathVariable Long userId) {
        return addressService.getDefaultAddress(userId);
    }
}
